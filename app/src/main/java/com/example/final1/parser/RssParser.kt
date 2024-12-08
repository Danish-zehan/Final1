fun parseRssFeed(responseBody: ResponseBody): List<Article> {
    val articles = mutableListOf<Article>()
    val parser = XmlPullParserFactory.newInstance().newPullParser()
    parser.setInput(responseBody.charStream())

    var eventType = parser.eventType
    var currentArticle: Article? = null

    while (eventType != XmlPullParser.END_DOCUMENT) {
        val tagName = parser.name
        when (eventType) {
            XmlPullParser.START_TAG -> {
                if (tagName.equals("item", ignoreCase = true)) {
                    currentArticle = Article()
                }
            }
            XmlPullParser.TEXT -> {
                currentArticle?.let {
                    when (tagName) {
                        "title" -> it.title = parser.text
                        "description" -> it.description = parser.text
                        "pubDate" -> it.pubDate = parser.text
                        "link" -> it.link = parser.text
                    }
                }
            }
            XmlPullParser.END_TAG -> {
                if (tagName.equals("item", ignoreCase = true)) {
                    currentArticle?.let { articles.add(it) }
                }
            }
        }
        eventType = parser.next()
    }
    return articles
}