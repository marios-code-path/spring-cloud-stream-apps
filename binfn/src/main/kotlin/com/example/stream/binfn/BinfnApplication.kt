package com.example.stream.binfn

import org.springframework.aot.hint.ExecutableMode
import org.springframework.aot.hint.RuntimeHints
import org.springframework.aot.hint.RuntimeHintsRegistrar
import org.springframework.aot.hint.TypeReference
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ImportRuntimeHints
import org.springframework.util.LinkedMultiValueMap
import java.util.*
import java.util.function.Function

@SpringBootApplication
@ImportRuntimeHints(MyRuntimeHints::class)
class BinfnApplication {

	@Bean
	fun binFn(): Function<String, String> = Function { stringToBinary(it) }

	fun stringToBinary(input: String): String {
		val binary = StringBuilder()
		input.forEach {
			binary.append(Integer.toBinaryString(it.code))
		}
		return binary.toString()
	}

}

fun main(args: Array<String>) {
	runApplication<BinfnApplication>(*args)
}


object MyRuntimeHints : RuntimeHintsRegistrar {
	override fun registerHints(hints: RuntimeHints, classLoader: ClassLoader?) {
		// Register method for reflection
		hints.reflection().registerType(TypeReference.of(LinkedMultiValueMap::class.java))
		Arrays.stream(LinkedMultiValueMap::class.java.declaredConstructors)
				.forEach { hints.reflection().registerConstructor(it, ExecutableMode.INVOKE) }
		Arrays.stream(LinkedMultiValueMap::class.java.declaredMethods)
				.forEach { hints.reflection().registerMethod(it, ExecutableMode.INVOKE) }
	}
}