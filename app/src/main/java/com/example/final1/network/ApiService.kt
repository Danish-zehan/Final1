interface ApiService {
    @GET("rss")
    suspend fun getNewsFeed(): Response<ResponseBody>
}