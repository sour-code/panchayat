package com.example.coro1


import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface APIService {
    @Headers("Content-Type:application/json", "Authorization:key=AAAAkh6o-4A:APA91bFZvpJwn3SvGNvPfqkVhEYpUidcOrI78zeQXOMPdg3sQyKhmrPoQjFlboC90iPBxFYjsB1ALpPm3sZU-gnKF4fOrWuVV_DO2iV8ZuJ4UB65ITFR_91fyim8ADNn2Z25cgluWXZd")
    @POST("fcm/send")
    fun sendNotification(@Body body: Sender): Call<MyResponse>

}