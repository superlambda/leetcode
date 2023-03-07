package inverview.dynamicproxy;

import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DynamicProxyTest {

	private static Logger LOGGER = LoggerFactory.getLogger(DynamicInvocationHandler.class);

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {

//		/* Case 1 */
		Map<String, String> proxyInstance = (Map<String, String>) Proxy.newProxyInstance(
				DynamicProxyTest.class.getClassLoader(), new Class[] { Map.class }, new DynamicInvocationHandler());
		for (Type type : proxyInstance.getClass().getGenericInterfaces()) {
			System.out.println("GenericInterfaces: " + type);
		}
		System.out.println("GenericSuperclass: " + proxyInstance.getClass().getGenericSuperclass());
		System.out.println("Superclass: " + proxyInstance.getClass().getSuperclass());
		proxyInstance.put("hello", "world");
//		
//
		/* Case 2 */
		/* Invocation Handler via Lambda Expressions */
		Map<String, String> proxyInstanceWithLambda = (Map<String, String>) Proxy.newProxyInstance(
				DynamicProxyTest.class.getClassLoader(), new Class[] { Map.class }, (proxy, method, methodArgs) -> {
					if (method.getName().equals("get")) {
						LOGGER.info("Invoked method: {}", method.getName());
						return 42;
					} else if (method.getName().equals("put")) {
						LOGGER.info("Invoked method: {}", method.getName());
						return 24;
					} else {
						throw new UnsupportedOperationException("Unsupported method: " + method.getName());
					}
				});
//		proxyInstanceWithLambda.get("hello");
//		proxyInstanceWithLambda.put("hello", "Unsupported method put");

		/* Case 3 */
//		Map<String, String> mapProxyInstance = (Map<String, String>) Proxy.newProxyInstance(
//				DynamicProxyTest.class.getClassLoader(),
//				new Class[] { Map.class }, 
//				new TimingDynamicInvocationHandler(new HashMap<>()));
//
//		mapProxyInstance.put("hello", "world");
//
//		CharSequence csProxyInstance = (CharSequence) Proxy.newProxyInstance(
//				DynamicProxyTest.class.getClassLoader(),
//				new Class[] { CharSequence.class },
//				new TimingDynamicInvocationHandler("Hello World"));
//
//		csProxyInstance.length();

	}
}
