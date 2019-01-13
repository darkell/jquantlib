package org.jquantlib

import com.fasterxml.jackson.core.type.TypeReference
import org.jquantlib.api.jackson.QuantlibObjectMapperFactory
import java.io.File

object DataLoader {

  val dataPath = "../../../PycharmProjects/Quantlib/target"

  val mapper = QuantlibObjectMapperFactory.build()

  fun <T> data(fileName: String, typeReference: TypeReference<T>): T {
    return mapper
        .readerFor(typeReference)
        .readValue<T>(File(dataPath, fileName))
  }

}