package com.study.reactivespring;

import reactor.core.publisher.Flux;

public class FluxMonoPoc {

	public static void main(String[] args) {
		
		Flux<String> programmingLanguages = Flux.just("Java", "NodeJS", "Rust", "Scala")
				.log();
		programmingLanguages.subscribe(System.out::println);
		/**
		 * 	21:00:49.467 [main] DEBUG reactor.util.Loggers$LoggerFactory - Using Slf4j logging framework
			21:00:49.508 [main] INFO reactor.Flux.Array.1 - | onSubscribe([Synchronous Fuseable] FluxArray.ArraySubscription)
			21:00:49.513 [main] INFO reactor.Flux.Array.1 - | request(unbounded)
			21:00:49.514 [main] INFO reactor.Flux.Array.1 - | onNext(Java)
			Java
			21:00:49.515 [main] INFO reactor.Flux.Array.1 - | onNext(NodeJS)
			NodeJS
			21:00:49.515 [main] INFO reactor.Flux.Array.1 - | onNext(Rust)
			Rust
			21:00:49.515 [main] INFO reactor.Flux.Array.1 - | onNext(Scala)
			Scala
			21:00:49.516 [main] INFO reactor.Flux.Array.1 - | onComplete()
		 */
		System.out.println("-------------------------");
		Flux<String> words = Flux.just("Apple", "Cat", "Bus")
				.concatWith(Flux.error(new RuntimeException("Failed")))
				.log();
		
		words.subscribe(System.out::println,
				(e) -> System.err.println("Error: " + e));
		
		/**
			21:03:49.477 [main] INFO reactor.Flux.ConcatArray.2 - onSubscribe(FluxConcatArray.ConcatArraySubscriber)
			21:03:49.478 [main] INFO reactor.Flux.ConcatArray.2 - request(unbounded)
			21:03:49.478 [main] INFO reactor.Flux.ConcatArray.2 - onNext(Apple)
			Apple
			21:03:49.478 [main] INFO reactor.Flux.ConcatArray.2 - onNext(Cat)
			Cat
			21:03:49.478 [main] INFO reactor.Flux.ConcatArray.2 - onNext(Bus)
			Bus
			21:03:49.484 [main] ERROR reactor.Flux.ConcatArray.2 - onError(java.lang.RuntimeException: Failed)
			21:03:49.487 [main] ERROR reactor.Flux.ConcatArray.2 - 
			java.lang.RuntimeException: Failed
				at com.study.reactivespring.FluxMonoPoc.main(FluxMonoPoc.java:28)
			Error: java.lang.RuntimeException: Failed
		 */
		
		
		System.out.println("-------------------------");
		Flux<String> programmingLanguages2 = Flux.just("Java", "NodeJS", "Rust", "Scala")
				.concatWith(Flux.just("LOL"))
				.log();
		programmingLanguages2.subscribe(System.out::println,
				(e) -> System.err.println("Error: " + e),
				() -> System.out.println("The End"));
		/**
			21:06:50.401 [main] INFO reactor.Flux.ConcatArray.3 - onSubscribe(FluxConcatArray.ConcatArraySubscriber)
			21:06:50.401 [main] INFO reactor.Flux.ConcatArray.3 - request(unbounded)
			21:06:50.402 [main] INFO reactor.Flux.ConcatArray.3 - onNext(Java)
			Java
			21:06:50.402 [main] INFO reactor.Flux.ConcatArray.3 - onNext(NodeJS)
			NodeJS
			21:06:50.402 [main] INFO reactor.Flux.ConcatArray.3 - onNext(Rust)
			Rust
			21:06:50.402 [main] INFO reactor.Flux.ConcatArray.3 - onNext(Scala)
			Scala
			21:06:50.419 [main] INFO reactor.Flux.ConcatArray.3 - onNext(LOL)
			LOL
			21:06:50.420 [main] INFO reactor.Flux.ConcatArray.3 - onComplete()
			The End
		 */
	}
	
	
}
