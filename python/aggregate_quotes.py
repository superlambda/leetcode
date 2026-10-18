import math
from datetime import datetime
from typing import Any, Dict, List, Optional

def _is_valid_name(value: Any) -> bool:
    """客户 Id / 保险公司名: 必须是去除首尾空白后非空的字符串 """
    return isinstance(value, str) and value.strip() != ""

def _is_valid_premium(value: Any) -> bool:
    """金额: 必须是有限的,非负的 int/float, bool 是int的子类, 必须显式排除"""
    if isinstance(value, bool) or not isinstance(value, (int, float)):
        return False
    # int 一定是有限的, 只有float 才可能出现nan/inf
    if isinstance(value, float) and not math.isfinite(value):
        return False
    return value >= 0

def _parse_timestamp(value: Any) -> Optional[datetime]:
    """解析带时区的ISO 8601 时间字符串, 非法返回 None."""
    if not isinstance(value, str) or not value:
        return None
    # datetime.fromisoformat 在 Python 3.11+ 才支持 "Z", 统一换算为 +00:00
    text = value[:-1] + "+00:00" if value.endswith("Z") else value
    try:
        dt = datetime.fromisoformat(text)
    except ValueError:
        return None
    if dt.tzinfo is None: # 缺少时区信息 -> 非法
        return None
    return dt

def _validate_quote(quote: Any):
    """校验单条报价, 合法时返回 (customer_id, provider, premium, timestamp), 否则返回 None"""
    if not isinstance(quote, dict):
        return None
    customer_id = quote.get("customer_id")
    provider = quote.get("provider")
    premium = quote.get("premium")
    timestamp = quote.get("timestamp")

    if not _is_valid_name(customer_id) or not _is_valid_name(provider):
        return None

    if not _is_valid_premium(premium):
        return None

    ts = _parse_timestamp(timestamp)
    if ts is None:
        return None
    return customer_id, provider, premium, ts

def aggregate_quotes(quotes: List[Dict[str, Any]]) -> Dict[str, Dict[str, Any]]:
    """
    Validate and aggregate insurance quotes.

    Args:
        quotes: A list of quote dictionaries.

    Returns:
        A dictionary keyed by customer_id.
    """
    if not isinstance(quotes, list):
        raise TypeError(f"quotes must be a list, got {type(quotes).__name__}")
    
    # customer_id -> provider -> {"premium": ..., "timestamp": ...}
    latest: Dict[str, Dict[str, Dict[str, Any]]] = {}
    for quote in quotes:
        parsed = _validate_quote(quote)
        if parsed is None:
            continue #无效报价,不影响整个批次
        customer_id, provider, premium, ts = parsed

        by_provider = latest.setdefault(customer_id, {})
        existing = by_provider.get(provider)
        if existing is None or ts > existing["timestamp"]:
            by_provider[provider] = {"premium": premium, "timestamp": ts}

    result: Dict[str, Dict[str, Any]] = {}

    for customer_id, by_provider in latest.items():
        total = sum(info["premium"] for info in by_provider.values())
        cheapest_provider = None
        cheapest_premium = None
        for provider, info in by_provider.items():
            # 元组比较: 金额低的优先; 金额相同时按字典序取最靠前的 provider
            if (cheapest_premium is None or (info["premium"], provider) < (cheapest_premium, cheapest_provider)):
                cheapest_premium = info["premium"]
                cheapest_provider = provider
        result[customer_id] = {
            "quote_count": len(by_provider),
            "total_premium": total,
            "cheapest_provider": cheapest_provider,
            "cheapest_premium": cheapest_premium
        }
    return result

if __name__ == "__main__":
    import pprint
    example = [
        {"customer_id": "C001", "provider": "ProviderA", "premium": 120.50,
            "timestamp": "2026-10-09T10:00:00Z"},
        {"customer_id": "C001", "provider": "ProviderB", "premium": 100.00,
            "timestamp": "2026-10-09T10:05:00Z"},
        {"customer_id": "C001", "provider": "ProviderA", "premium": 110.00,
            "timestamp": "2026-10-09T10:10:00Z"},
        {"customer_id": "C002", "provider": "ProviderC", "premium": 200.00,
            "timestamp": "2026-10-09T10:00:00Z"},
        {"customer_id": "C002", "provider": "ProviderD", "premium": 200.00,
            "timestamp": "2026-10-09T10:00:00Z"},
        {"customer_id": "C003", "provider": "ProviderE", "premium": float("nan"),
            "timestamp": "2026-10-09T10:00:00Z"},
    ]
    pprint.pprint(aggregate_quotes(example))