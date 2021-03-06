package com.ishikota.photoviewerandroid.sampledata

import com.ishikota.photoviewerandroid.data.api.entities.Photo
import com.ishikota.photoviewerandroid.di.NetworkModule

inline fun<reified T> jsonToModel(json: String): T? = NetworkModule(
    apiEndpoint = "https://dummy.com/",
    oauthEndpoint = "https://dummy.com",
    appAccessKey = "dummy_key"
).provideMoshi().adapter(T::class.java).fromJson(json)

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
            "liked_by_user":false,
            "alt_description":"man on black cruiser motorcycle in highway",
            "current_user_collections": [
                {
                    "id": 206,
                    "title": "Makers: Cat and Ben",
                    "published_at": "2016-01-12T18:16:09-05:00",
                    "updated_at": "2016-07-10T11:00:01-05:00",
                    "total_photos" : 10,
                    "user":{
                        "id":"XnhDFu3Jr-A",
                        "username":"harleydavidson",
                        "name":"Harley-Davidson",
                        "total_collections":2,
                        "total_likes":46,
                        "total_photos":53,
                        "profile_image" : {
                            "small": "https://dummy.png",
                            "medium" : "https://dummy.png",
                            "large" : "https://dummy.png"
                        }
                    },
                    "cover_photo": {
                        "id":"Be8TdJZPaBE",
                        "created_at":"2019-05-27T14:07:14-04:00",
                        "updated_at":"2019-12-21T00:04:32-05:00",
                        "width":8192,
                        "height":5462,
                        "color":"#F7FBFD",
                        "likes":219,
                        "liked_by_user":false,
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
                            "total_photos":53,
                            "profile_image" : {
                                "small": "https://dummy.png",
                                "medium" : "https://dummy.png",
                                "large" : "https://dummy.png"
                            }
                        },
                        "current_user_collections": []
                    }
                }
            ],
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
                "total_photos":53,
                "profile_image" : {
                    "small": "https://dummy.png",
                    "medium" : "https://dummy.png",
                    "large" : "https://dummy.png"
                }
            },
            "tags":[
                { "title": "tag1" },
                { "title": "tag2" }
            ]
        }
    """.trimIndent()
