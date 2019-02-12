package org.jquantlib

import com.fasterxml.jackson.core.type.TypeReference
import org.jquantlib.api.jackson.QuantlibObjectMapperFactory
import org.junit.Assert.assertEquals
import java.io.File
import java.time.LocalDate

object DataLoader {

  val dataPath = "../../../PycharmProjects/Quantlib/target/"

  val mapper = QuantlibObjectMapperFactory.build()

  val error = 1e-10

  fun <T> data(fileName: String, typeReference: TypeReference<T>): T {
    return mapper
        .readerFor(typeReference)
        .readValue<T>(File(dataPath, fileName))
  }

  fun <T> dataForEach(filename: String, tr: TypeReference<List<T>>, f: (T) -> Unit) {
    data(filename, tr).forEach { f(it) }
  }

  fun <T: ExpectedAsDouble> assertEqualsDouble(filename: String, tr: TypeReference<List<T>>, f: (T) -> Double) {
    data(filename, tr).forEach {
      assertEquals(
          it.toString(),
          it.expected,
          f(it),
          error
      )
    }
  }

  fun <T: ExpectedAsAny<*>> assertEqualsAny(filename: String, tr: TypeReference<List<T>>, f: (T) -> Any) {
    data(filename, tr).forEach {
      assertEquals(
          it.toString(),
          it.expected,
          f(it)
      )
    }
  }

}

interface ExpectedAsDouble {
  val expected: Double
}

interface ExpectedAsAny<T> {
  val expected: T
}