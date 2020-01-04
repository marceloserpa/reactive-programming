package com.study.reactivespring;

import org.junit.Test;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class MonoTest {

	
	@Test
	public void monoTest() {
	
		Mono<String> myName = Mono.just("Marcelo");
		
		StepVerifier.create(myName.log())
			.expectNext("Marcelo")
			.verifyComplete();
		/**
			21:17:07.822 [main] DEBUG reactor.util.Loggers$LoggerFactory - Using Slf4j logging framework
			21:17:07.924 [main] INFO reactor.Mono.Just.1 - | onSubscribe([Synchronous Fuseable] Operators.ScalarSubscription)
			21:17:07.934 [main] INFO reactor.Mono.Just.1 - | request(unbounded)
			21:17:07.935 [main] INFO reactor.Mono.Just.1 - | onNext(Marcelo)
			21:17:07.938 [main] INFO reactor.Mono.Just.1 - | onComplete()
		 */
	}
	
	@Test
	public void monoErrorTest() {
		StepVerifier.create(Mono.error(new RuntimeException("Exception occurred")).log())
			.expectError(RuntimeException.class)
			.verify();
		/**
			21:18:51.580 [main] DEBUG reactor.util.Loggers$LoggerFactory - Using Slf4j logging framework
			21:18:51.661 [main] INFO reactor.Mono.Error.1 - onSubscribe([Fuseable] Operators.EmptySubscription)
			21:18:51.668 [main] INFO reactor.Mono.Error.1 - request(unbounded)
			21:18:51.670 [main] ERROR reactor.Mono.Error.1 - onError(java.lang.RuntimeException: Exception occurred)
			21:18:51.672 [main] ERROR reactor.Mono.Error.1 - 
			java.lang.RuntimeException: Exception occurred
				at com.study.reactivespring.MonoTest.monoErrorTest(MonoTest.java:30)
				at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)		 * 
		 */
	}
}
