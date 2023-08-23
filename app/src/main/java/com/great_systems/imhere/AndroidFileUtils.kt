package com.great_systems.imhere

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.content.res.AssetFileDescriptor
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.ContactsContract
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import java.io.FileDescriptor
import java.io.FileNotFoundException
import java.io.IOException
import java.util.*

class AndroidFileUtils(val context: Context) {

    // метод возвращает полный реальный путь до файла, включая имя и расширение
    fun getFilePath(uri: Uri): String {
        var uri = uri
        var selection: String? = null
        var selectionArgs: Array<String>? = null
        if (Build.VERSION.SDK_INT >= 19 && DocumentsContract.isDocumentUri(context, uri)) {
            if (isExternalStorageDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }
                    .toTypedArray()
                return "${Environment.getExternalStorageDirectory()}/" + split[1]
            } else if (isDownloadsDocument(uri)) {
                val id = DocumentsContract.getDocumentId(uri)
                uri = ContentUris.withAppendedId(
                    Uri.parse("content://downloads/public_downloads"),
                    java.lang.Long.valueOf(id)
                )
            } else if (isMediaDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }
                    .toTypedArray()
                val type = split[0]
                if ("image" == type) {
                    uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                } else if ("video" == type) {
                    uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                } else if ("audio" == type) {
                    uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                }
                selection = "_id=?"
                selectionArgs = arrayOf(split[1])
            }
        }
        if ("content".equals(uri.scheme, ignoreCase = true)) {
            val projection = arrayOf(MediaStore.Images.Media.DATA)
            var cursor: Cursor? = null
            try {
                cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null)
                val column_index: Int = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                if (cursor!!.moveToFirst()) {
                    return cursor!!.getString(column_index)
                }
            } catch (e: java.lang.Exception) {
            } finally {
                if (cursor != null) cursor.close()
            }
        } else if ("file".equals(uri.scheme, ignoreCase = true)) {
            return uri.path.toString()
        }
        return ""
    }

    fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }

    fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }

    fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }

    // метод возвращает из полного пути расширение файла
    fun getFileExtension(path: String): String? {
        val pos = path.lastIndexOf(".")
        return if (pos != -1) path.substring(pos + 1) else ""
    }
    fun getMimeType(uri: Uri): String {
        var mimeType = ""
        if (uri.scheme == ContentResolver.SCHEME_CONTENT) {
            val cr: ContentResolver = context.getContentResolver()
            mimeType = cr.getType(uri).toString()
        } else {
            val fileExtension = MimeTypeMap.getFileExtensionFromUrl(
                uri
                    .toString()
            )
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                fileExtension.lowercase(Locale.getDefault())
            ).toString()
        }
        return mimeType
    }

    // Получение фото (протестировано для фото контактов)
    fun loadContactPhotoThumbnail(photoData: String): Bitmap? {
        var afd: AssetFileDescriptor? = null
        try {
            val thumbUri: Uri
            thumbUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                Uri.parse(photoData)
            } else {
                val contactUri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, photoData)
                Uri.withAppendedPath(contactUri, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY)
            }
            afd = context.getContentResolver()
                .openAssetFileDescriptor(thumbUri, "r")
            val fileDescriptor: FileDescriptor? = afd?.fileDescriptor
            if (fileDescriptor != null) return BitmapFactory.decodeFileDescriptor(
                fileDescriptor,
                null,
                null
            )
        } catch (e: FileNotFoundException) {
        } finally {
            if (afd != null) {
                try {
                    afd.close()
                } catch (e: IOException) {
                }
            }
        }
        return null
    }
}