package com.ishikota.photoviewerandroid.sampledata

import com.ishikota.photoviewerandroid.data.api.entities.Photo
import com.ishikota.photoviewerandroid.infra.moshi.buildDefaultMoshi

inline fun<reified T> jsonToModel(json: String): T? = buildDefaultMoshi().adapter(T::class.java).fromJson(json)

fun buildSamplePhoto(): Photo = jsonToModel<Photo>(photoJson)!!
private val photoJson =
    """
        {
            "id":"Be8TdJZPaBE",
            "created_at":"2019-05-27T14:07:14-04:00",
            "updated_at":"2019-12-21T00:04:32-05:00",
            "width":8192,
            "height":5462,
            "color":"#F7FBFD",
            "likes":219,
            "alt_description":"man on black cruiser motorcycle in highway",
            "urls":{
                "raw":"https://images.unsplash.com/photo-1558980394-0a06c4631733?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEwNzMzM30",
                "full":"https://images.unsplash.com/photo-1558980394-0a06c4631733?ixlib=rb-1.2.1&q=85&fm=jpg&crop=entropy&cs=srgb&ixid=eyJhcHBfaWQiOjEwNzMzM30",
                "regular":"https://images.unsplash.com/photo-1558980394-0a06c4631733?ixlib=rb-1.2.1&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=1080&fit=max&ixid=eyJhcHBfaWQiOjEwNzMzM30",
                "small":"https://images.unsplash.com/photo-1558980394-0a06c4631733?ixlib=rb-1.2.1&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=400&fit=max&ixid=eyJhcHBfaWQiOjEwNzMzM30",
                "thumb":"https://images.unsplash.com/photo-1558980394-0a06c4631733?ixlib=rb-1.2.1&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=200&fit=max&ixid=eyJhcHBfaWQiOjEwNzMzM30"
            },
            "user":{
                "id":"XnhDFu3Jr-A",
                "username":"harleydavidson",
                "name":"Harley-Davidson",
                "total_collections":2,
                "total_likes":46,
                "total_photos":53
            }
        }
    """.trimIndent()
