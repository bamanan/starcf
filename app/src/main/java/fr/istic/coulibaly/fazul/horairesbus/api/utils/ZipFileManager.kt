package fr.istic.coulibaly.fazul.horairesbus.api.utils

import android.util.Log
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.zip.ZipInputStream

object ZipFileManager {
    private val BUFFER_SIZE = ByteArray(6 * 1024)
    private val TAG = "Unzipping"

    val unzip = fun(file: File): List<String> {
        // Create Directory where to unzip files
        val directory = File(file.path.removeSuffix(".zip"))
        if (!directory.isDirectory) {
            directory.mkdirs()
        }
        val unziped = mutableListOf<String>()

        val zipInputStream = ZipInputStream(FileInputStream(file))

        try {
            zipInputStream.use {
                generateSequence { it.nextEntry }
                    .forEach { nextEntry ->
                        val nextEntryFileName = nextEntry.name
                        val path = directory.path + File.separator + nextEntryFileName
                        val myFile = File(path)
                        val outputFileStream = FileOutputStream(myFile, false)
                        while (true) {
                            val read = zipInputStream.read(BUFFER_SIZE)
                            if (read == -1) break
                            outputFileStream.write(BUFFER_SIZE, 0, read)
                        }
                        unziped.add(nextEntryFileName)
                        outputFileStream.close()
                    }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Unzip exception", e)
        } finally {
            zipInputStream.close()
        }
        return unziped
    }
}