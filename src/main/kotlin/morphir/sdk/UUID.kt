/*
Copyright 2020 Morgan Stanley

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package morphir.sdk

import java.util.UUID
import java.security.MessageDigest
import java.nio.ByteBuffer

/**
 * UUID utilities for working with universally unique identifiers.
 */
object UUIDUtils {
    
    /**
     * The error type for UUIDs
     */
    sealed class Error {
        object WrongFormat : Error()
        object WrongLength : Error()
        object UnsupportedVariant : Error()
        object IsNil : Error()
        object NoVersion : Error()
    }
    
    /**
     * You can attempt to create a UUID from a string. This function can interpret a fairly broad range of 
     * formatted (and mis-formatted) UUIDs, including ones with too much whitespace, too many (or not enough) 
     * hyphens, or uppercase characters.
     */
    fun parse(str: String): Result<UUID> = try {
        val cleanedStr = str.trim().replace("-", "").uppercase()
        if (cleanedStr.length != 32) {
            Result.failure(Error.WrongLength)
        } else {
            val formatted = "${cleanedStr.substring(0, 8)}-${cleanedStr.substring(8, 12)}-${cleanedStr.substring(12, 16)}-${cleanedStr.substring(16, 20)}-${cleanedStr.substring(20, 32)}"
            Result.success(UUID.fromString(formatted))
        }
    } catch (e: Exception) {
        Result.failure(Error.WrongFormat)
    }
    
    /**
     * Includes all the functionality as `parse`, however only returns a nullable UUID on failures instead of an Error.
     */
    fun fromString(str: String): UUID? = parse(str).getOrNull()
    
    /**
     * Create a version 5 UUID from a String and a namespace, which should be a UUID. The same name and namespace 
     * will always produce the same UUID, which can be used to your advantage. Furthermore, the UUID created from 
     * this can be used as a namespace for another UUID, creating a hierarchy of sorts.
     */
    fun forName(name: String, namespace: UUID): UUID {
        val md = MessageDigest.getInstance("SHA-1")
        
        // Convert namespace UUID to bytes
        val namespaceBytes = ByteBuffer.allocate(16)
            .putLong(namespace.mostSignificantBits)
            .putLong(namespace.leastSignificantBits)
            .array()
        
        md.update(namespaceBytes)
        md.update(name.toByteArray(Charsets.UTF_8))
        val hash = md.digest()
        
        // Set version (5) and variant bits
        hash[6] = (hash[6].toInt() and 0x0f or 0x50).toByte()
        hash[8] = (hash[8].toInt() and 0x3f or 0x80).toByte()
        
        val buffer = ByteBuffer.wrap(hash)
        val mostSigBits = buffer.long
        val leastSigBits = buffer.long
        
        return UUID(mostSigBits, leastSigBits)
    }
    
    /**
     * The canonical representation of the UUID
     */
    fun toString(uuid: UUID): String = uuid.toString()
    
    /**
     * Get the version number of a UUID. Only versions 3, 4, and 5 are supported.
     */
    fun version(uuid: UUID): Int = uuid.version()
    
    /**
     * Returns the relative ordering of two UUIDs. The main use case of this function is helping in 
     * binary-searching algorithms.
     */
    fun compare(uuid1: UUID, uuid2: UUID): Int = uuid1.compareTo(uuid2)
    
    /**
     * Returns a nil UUID.
     */
    val nilString: String = "00000000-0000-0000-0000-000000000000"
    
    /**
     * True if the given string represents the nil UUID (00000000-0000-0000-0000-000000000000).
     */
    fun isNilString(str: String): Boolean = str.replace("-", "").equals("00000000000000000000000000000000", ignoreCase = true)
    
    /**
     * A UUID for the DNS namespace, "6ba7b810-9dad-11d1-80b4-00c04fd430c8".
     */
    val dnsNamespace: UUID = UUID.fromString("6ba7b810-9dad-11d1-80b4-00c04fd430c8")
    
    /**
     * A UUID for the URL namespace, "6ba7b811-9dad-11d1-80b4-00c04fd430c8".
     */
    val urlNamespace: UUID = UUID.fromString("6ba7b811-9dad-11d1-80b4-00c04fd430c8")
    
    /**
     * A UUID for the ISO object ID (OID) namespace, "6ba7b812-9dad-11d1-80b4-00c04fd430c8".
     */
    val oidNamespace: UUID = UUID.fromString("6ba7b812-9dad-11d1-80b4-00c04fd430c8")
    
    /**
     * A UUID for the X.500 Distinguished Name (DN) namespace, "6ba7b814-9dad-11d1-80b4-00c04fd430c8".
     */
    val x500Namespace: UUID = UUID.fromString("6ba7b814-9dad-11d1-80b4-00c04fd430c8")
}