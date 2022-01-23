package fr.istic.mob.starcf.api.utils

import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.zip.ZipFile

object ZipFileManager {
    val BUFFER_SIZE = ByteArray(4 * 1024)
     const val TAG = "Unzipping"

    fun zipFile(fileName: String): ZipFile {
        val file = File(fileName)
        val directoryName = fileName.removeSuffix(".zip")
        val directory = File(directoryName)
        if (!directory.isDirectory) directory.mkdirs()

        return ZipFile(file)
    }

    fun unzip(fileName: String): Boolean {
        val file = File(fileName)
        val directoryName = fileName.removeSuffix(".zip")
        val directory = File(directoryName)
        if (!directory.isDirectory) directory.mkdirs()

        try {
            ZipFile(file).use { zip ->
                zip.entries().asSequence().forEach { entry ->
                    zip.getInputStream(entry).use { input ->
                        val filePath = directoryName + File.separator + entry.name
                        val bufferedOutputStream = BufferedOutputStream(FileOutputStream(filePath))
                        var read: Int = 0
                        while (input.read(BUFFER_SIZE).also { read = it } != -1) {
                            bufferedOutputStream.write(BUFFER_SIZE, 0, read)
                        }
                        bufferedOutputStream.close()
                    }
                }
            }
        } catch (e: IOException) {
            return false
        }


        return true
    }

}