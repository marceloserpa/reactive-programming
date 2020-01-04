package com.study.reactivespring;

import org.junit.Test;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class FluxMonoTest {

	@Test
	public void fluxTest() {
		Flux<String> programmingLanguages = Flux.just("Java", "NodeJS", "Rust", "Scala")
				.concatWith(Flux.just("LOL"))
				.log();
		
		StepVerifier.create(programmingLanguages)
			.expectNext("Java")
			.expectNext("NodeJS")
			.expectNext("Rust")
			.expectNext("Scala")
			.expectNext("LOL")
			.verifyComplete(); // Starts to flow
	}
	
	@Test
	public void fluxWithErrorClassTest() {
		Flux<String> programmingLanguages = Flux.just("Java", "NodeJS", "Rust", "Scala")
				.concatWith(Flux.error(new RuntimeException("Error happen :/")))
				.log();
		
		StepVerifier.create(programmingLanguages)
			.expectNext("Java")
			.expectNext("NodeJS")
			.expectNext("Rust")
			.expectNext("Scala")
			.expectError(RuntimeException.class)
			.verify(); // Starts to flow
	}
	
	@Test
	public void fluxWithErrorMessageTest() {
		Flux<String> programmingLanguages = Flux.just("Java", "NodeJS", "Rust", "Scala")
				.concatWith(Flux.error(new RuntimeException("Error happen :/")))
				.log();
		
		StepVerifier.create(programmingLanguages)
			.expectNext("Java")
			.expectNext("NodeJS")
			.expectNext("Rust")
			.expectNext("Scala")
			.expectErrorMessage("Error happen :/")
			.verify(); // Starts to flow
	}
		
}
