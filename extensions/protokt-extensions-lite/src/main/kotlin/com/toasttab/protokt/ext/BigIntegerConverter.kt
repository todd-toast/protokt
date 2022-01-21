package com.toasttab.protokt.ext

import com.google.auto.service.AutoService

@AutoService(Converter::class)
object BigIntegerConverter : Converter<java.math.BigInteger, BigInteger> {
    override val wrapped: KClass<BigInteger> = BigInteger::class
    override val wrapper: KClass<java.math.BigInteger> = java.math.BigInteger::class

    override fun unwrap(wrapped: java.math.BigInteger): BigInteger = BigInteger {
        value = Bytes(wrapped.toByteArray())
    }

    override fun wrap(unwrapped: BigInteger): java.math.BigInteger = java.math.BigInteger(unwrapped.value.bytes)
}
