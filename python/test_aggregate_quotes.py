"""aggregate_quotes 的自测用例, 直接运行: python3 test_aggregate_quotes.py"""

import copy
import math

from aggregate_quotes import aggregate_quotes


def quote(cid="C001", provider="ProviderA", premium=100.0, ts="2026-10-09T10:00:00Z"):
    """构造一条合法报价, 便于测试中只覆盖某一个字段。"""
    return {
        "customer_id": cid,
        "provider": provider,
        "premium": premium,
        "timestamp": ts,
    }


# ---------- 题目给出的示例 ----------

def test_example():
    quotes = [
        quote("C001", "ProviderA", 120.50, "2026-10-09T10:00:00Z"),
        quote("C001", "ProviderB", 100.00, "2026-10-09T10:05:00Z"),
        quote("C001", "ProviderA", 110.00, "2026-10-09T10:10:00Z"),
        quote("C002", "ProviderC", 200.00, "2026-10-09T10:00:00Z"),
        quote("C002", "ProviderD", 200.00, "2026-10-09T10:00:00Z"),
        quote("C003", "ProviderE", float("nan"), "2026-10-09T10:00:00Z"),
    ]
    expected = {
        "C001": {
            "quote_count": 2,
            "total_premium": 210.0,
            "cheapest_provider": "ProviderB",
            "cheapest_premium": 100.0,
        },
        "C002": {
            "quote_count": 2,
            "total_premium": 400.0,
            "cheapest_provider": "ProviderC",
            "cheapest_premium": 200.0,
        },
    }
    assert aggregate_quotes(quotes) == expected


# ---------- 输入类型校验 ----------

def test_empty_list_returns_empty_dict():
    assert aggregate_quotes([]) == {}


def test_non_list_raises_type_error():
    for bad in (None, "x", 42, 3.14, {"customer_id": "C001"}, (quote(),)):
        try:
            aggregate_quotes(bad)
        except TypeError:
            pass
        else:
            raise AssertionError(f"expected TypeError for {bad!r}")


def test_non_dict_entries_skipped():
    quotes = [quote(), None, 42, "junk", [1, 2], quote("C002", "ProviderB", 50.0)]
    assert set(aggregate_quotes(quotes)) == {"C001", "C002"}


# ---------- 字段校验 ----------

def test_missing_or_wrong_type_fields_skipped():
    good = quote()
    cases = [
        {"provider": "P", "premium": 1.0, "timestamp": "2026-10-09T10:00:00Z"},   # 缺 customer_id
        {"customer_id": "C", "premium": 1.0, "timestamp": "2026-10-09T10:00:00Z"},  # 缺 provider
        {"customer_id": "C", "provider": "P", "timestamp": "2026-10-09T10:00:00Z"},  # 缺 premium
        {"customer_id": "C", "provider": "P", "premium": 1.0},                    # 缺 timestamp
        {**good, "customer_id": 123},          # ID 不是字符串
        {**good, "customer_id": ""},           # ID 为空
        {**good, "provider": None},            # provider 为 None
        {**good, "provider": "   "},           # provider 只有空白
        {**good, "premium": "100"},            # 金额是字符串
        {**good, "premium": None},             # 金额为 None
        {**good, "timestamp": 20261009},       # 时间不是字符串
    ]
    quotes = cases + [good]
    result = aggregate_quotes(quotes)
    assert list(result) == ["C001"]
    assert result["C001"]["quote_count"] == 1
    assert result["C001"]["total_premium"] == 100.0


def test_invalid_premiums_skipped():
    ts = "2026-10-09T10:00:00Z"
    invalid = [-1.0, -0.01, float("nan"), float("inf"), float("-inf"),
               True, False, "100", None, {"v": 1}]
    for p in invalid:
        result = aggregate_quotes([quote(premium=p, ts=ts)])
        assert result == {}, f"premium {p!r} should be rejected"


def test_valid_premium_forms_kept():
    ts = "2026-10-09T10:00:00Z"
    result = aggregate_quotes([
        quote("C001", "ProviderA", 0, ts),       # 0 合法
        quote("C001", "ProviderB", 100, ts),     # 整数合法
        quote("C002", "ProviderC", 0.5, ts),
    ])
    assert result["C001"]["cheapest_premium"] == 0
    assert result["C001"]["total_premium"] == 100  # 全整数时 sum 保持 int
    assert result["C002"]["cheapest_premium"] == 0.5


def test_timestamp_validation():
    valid_forms = [
        "2026-10-09T10:00:00Z",              # Z 表示 UTC
        "2026-10-09T10:00:00+00:00",         # 显式 +00:00
        "2026-10-09T18:00:00+08:00",         # 其他时区偏移
    ]
    for ts in valid_forms:
        assert aggregate_quotes([quote(ts=ts)]) != {}, f"timestamp {ts!r} should be accepted"
    invalid_forms = [
        "2026-10-09T10:00:00",   # 没有时区信息
        "2026-10-09",            # 只有日期
        "2026/10/09 10:00:00",   # 非 ISO 格式
        "10:00:00",              # 只有时间
        "not a time",
        "",
        None,
    ]
    for ts in invalid_forms:
        assert aggregate_quotes([quote(ts=ts)]) == {}, f"timestamp {ts!r} should be rejected"


# ---------- 去重与覆盖规则 ----------

def test_newer_quote_wins_regardless_of_input_order():
    # 10:00+05:00 == 05:00 UTC; 09:00Z == 09:00 UTC, 实际上更晚
    older = "2026-10-09T10:00:00+05:00"
    newer = "2026-10-09T09:00:00Z"
    # 新的在前、旧的在后 -> 仍保留新的
    result = aggregate_quotes([
        quote("C001", "ProviderA", 110.0, newer),
        quote("C001", "ProviderA", 90.0, older),
    ])
    assert result["C001"] == {
        "quote_count": 1,
        "total_premium": 110.0,
        "cheapest_provider": "ProviderA",
        "cheapest_premium": 110.0,
    }
    # 旧的在先 -> 新的覆盖
    result2 = aggregate_quotes([
        quote("C001", "ProviderA", 90.0, older),
        quote("C001", "ProviderA", 110.0, newer),
    ])
    assert result2["C001"]["total_premium"] == 110.0


def test_same_timestamp_keeps_first_valid_quote():
    ts = "2026-10-09T10:00:00Z"
    # 同时间、不同金额 -> 保留先出现的
    result = aggregate_quotes([
        quote("C001", "ProviderA", 100.0, ts),
        quote("C001", "ProviderA", 999.0, ts),
    ])
    assert result["C001"]["total_premium"] == 100.0
    # 先出现的是无效报价 -> 保留后出现的有效报价(即第一条有效报价)
    result2 = aggregate_quotes([
        quote("C001", "ProviderA", float("nan"), ts),
        quote("C001", "ProviderA", 80.0, ts),
    ])
    assert result2["C001"]["total_premium"] == 80.0


def test_tie_picks_lexicographically_smallest_provider():
    ts = "2026-10-09T10:00:00Z"
    result = aggregate_quotes([
        quote("C001", "ProviderB", 200.0, ts),
        quote("C001", "ProviderA", 200.0, ts),
        quote("C001", "ProviderC", 150.0, ts),  # 更低的, 与平局无关
    ])
    assert result["C001"]["cheapest_provider"] == "ProviderC"
    result2 = aggregate_quotes([
        quote("C001", "ProviderB", 200.0, ts),
        quote("C001", "ProviderA", 200.0, ts),
    ])
    assert result2["C001"]["cheapest_provider"] == "ProviderA"


# ---------- 聚合与工程性 ----------

def test_interleaved_customers():
    result = aggregate_quotes([
        quote("C001", "ProviderA", 100.0, "2026-10-09T10:00:00Z"),
        quote("C002", "ProviderX", 50.0, "2026-10-09T10:00:00Z"),
        quote("C001", "ProviderB", 60.0, "2026-10-09T10:01:00Z"),
        quote("C002", "ProviderY", 70.0, "2026-10-09T10:02:00Z"),
        quote("C001", "ProviderA", 999.0, "2026-10-09T09:59:00Z"),  # 旧报价, 不能覆盖
    ])
    assert result["C001"]["quote_count"] == 2
    assert result["C001"]["total_premium"] == 160.0
    assert result["C001"]["cheapest_provider"] == "ProviderB"
    assert result["C002"]["cheapest_provider"] == "ProviderX"


def test_customer_with_only_invalid_quotes_is_omitted():
    result = aggregate_quotes([
        quote("C001", "ProviderA", -5.0),                            # 金额非法
        quote("C001", "ProviderB", 10.0, "2026-10-09T10:00:00"),     # 时间无时区
    ])
    assert result == {}


def test_input_not_mutated():
    quotes = [
        quote("C001", "ProviderA", 100.0),
        quote("C001", "ProviderA", 90.0, "2026-10-09T11:00:00Z"),
    ]
    first = quotes[0]
    snapshot = copy.deepcopy(quotes)
    aggregate_quotes(quotes)
    assert quotes == snapshot      # 内容未变
    assert quotes[0] is first      # 原字典对象本身也没被替换


# ---------- 运行器 ----------

def main():
    tests = [(name, fn) for name, fn in sorted(globals().items())
             if name.startswith("test_") and callable(fn)]
    failed = 0
    for name, fn in tests:
        try:
            fn()
        except AssertionError as exc:
            failed += 1
            print(f"FAIL  {name}: {exc}")
        else:
            print(f"PASS  {name}")
    print(f"\n{len(tests) - failed}/{len(tests)} passed")
    raise SystemExit(1 if failed else 0)


if __name__ == "__main__":
    main()
