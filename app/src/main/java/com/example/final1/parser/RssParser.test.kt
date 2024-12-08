package com.example.final1.parser

import okhttp3.ResponseBody
import org.xmlpull.v1.XmlPullParserFactory
import java.io.StringReader

class RssParserTest {


@Test
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.*
import org.junit.Test

fun `should return an empty list when the input is an empty XML document`() {
    val emptyXmlDocument = ""
    val responseBody = emptyXmlDocument.toResponseBody("application/xml".toMediaTypeOrNull())
    val articles = parseRssFeed(responseBody)

    assertTrue(articles.isEmpty())
}

@Test
fun `should return an empty list when the RSS feed has no items`() {
    val emptyRssFeed = """
        <rss version="2.0">
            <channel>
                <title>Empty RSS Feed</title>
                <link>http://example.com</link>
                <description>This is an empty RSS feed</description>
            </channel>
        </rss>
    """.trimIndent()

    val responseBody = ResponseBody.create(null, emptyRssFeed)
    val articles = parseRssFeed(responseBody)

    assertTrue(articles.isEmpty())
}

@Test
fun `should correctly parse a single item with all fields present`() {
    val singleItemRssFeed = """
        <rss version="2.0">
            <channel>
                <title>Sample RSS Feed</title>
                <link>http://example.com</link>
                <description>This is a sample RSS feed</description>
                <item>
                    <title>Sample Article</title>
                    <description>This is a sample article description.</description>
                    <pubDate>Mon, 01 Jan 2023 00:00:00 GMT</pubDate>
                    <link>http://example.com/sample-article</link>
                </item>
            </channel>
        </rss>
    """.trimIndent()

    val responseBody = ResponseBody.create(null, singleItemRssFeed)
    val articles = parseRssFeed(responseBody)

    assertEquals(1, articles.size)
    val article = articles[0]
    assertEquals("Sample Article", article.title)
    assertEquals("This is a sample article description.", article.description)
    assertEquals("Mon, 01 Jan 2023 00:00:00 GMT", article.pubDate)
    assertEquals("http://example.com/sample-article", article.link)
}

@Test
fun `should handle an RSS feed with multiple items correctly`() {
    val multipleItemsRssFeed = """
        <rss version="2.0">
            <channel>
                <title>Sample RSS Feed</title>
                <link>http://example.com</link>
                <description>This is a sample RSS feed</description>
                <item>
                    <title>First Article</title>
                    <description>This is the first article description.</description>
                    <pubDate>Mon, 01 Jan 2023 00:00:00 GMT</pubDate>
                    <link>http://example.com/first-article</link>
                </item>
                <item>
                    <title>Second Article</title>
                    <description>This is the second article description.</description>
                    <pubDate>Tue, 02 Jan 2023 00:00:00 GMT</pubDate>
                    <link>http://example.com/second-article</link>
                </item>
            </channel>
        </rss>
    """.trimIndent()

    val responseBody = ResponseBody.create(null, multipleItemsRssFeed)
    val articles = parseRssFeed(responseBody)

    assertEquals(2, articles.size)

    val firstArticle = articles[0]
    assertEquals("First Article", firstArticle.title)
    assertEquals("This is the first article description.", firstArticle.description)
    assertEquals("Mon, 01 Jan 2023 00:00:00 GMT", firstArticle.pubDate)
    assertEquals("http://example.com/first-article", firstArticle.link)

    val secondArticle = articles[1]
    assertEquals("Second Article", secondArticle.title)
    assertEquals("This is the second article description.", secondArticle.description)
    assertEquals("Tue, 02 Jan 2023 00:00:00 GMT", secondArticle.pubDate)
    assertEquals("http://example.com/second-article", secondArticle.link)
}

@Test
fun `should ignore tags that are not item, title, description, pubDate, or link`() {
    val rssFeedWithExtraTags = """
        <rss version="2.0">
            <channel>
                <title>Sample RSS Feed</title>
                <link>http://example.com</link>
                <description>This is a sample RSS feed</description>
                <item>
                    <title>Sample Article</title>
                    <description>This is a sample article description.</description>
                    <pubDate>Mon, 01 Jan 2023 00:00:00 GMT</pubDate>
                    <link>http://example.com/sample-article</link>
                    <extraTag>This should be ignored</extraTag>
                </item>
            </channel>
        </rss>
    """.trimIndent()

    val responseBody = ResponseBody.create(null, rssFeedWithExtraTags)
    val articles = parseRssFeed(responseBody)

    assertEquals(1, articles.size)
    val article = articles[0]
    assertEquals("Sample Article", article.title)
    assertEquals("This is a sample article description.", article.description)
    assertEquals("Mon, 01 Jan 2023 00:00:00 GMT", article.pubDate)
    assertEquals("http://example.com/sample-article", article.link)
}

@Test
fun `should handle missing title tag gracefully within an item`() {
    val rssFeedWithMissingTitle = """
        <rss version="2.0">
            <channel>
                <title>Sample RSS Feed</title>
                <link>http://example.com</link>
                <description>This is a sample RSS feed</description>
                <item>
                    <description>This is a sample article description.</description>
                    <pubDate>Mon, 01 Jan 2023 00:00:00 GMT</pubDate>
                    <link>http://example.com/sample-article</link>
                </item>
            </channel>
        </rss>
    """.trimIndent()

    val responseBody = ResponseBody.create(null, rssFeedWithMissingTitle)
    val articles = parseRssFeed(responseBody)

    assertEquals(1, articles.size)
    val article = articles[0]
    assertNull(article.title)
    assertEquals("This is a sample article description.", article.description)
    assertEquals("Mon, 01 Jan 2023 00:00:00 GMT", article.pubDate)
    assertEquals("http://example.com/sample-article", article.link)
}

@Test
fun `should handle missing description tag gracefully within an item`() {
    val rssFeedWithMissingDescription = """
        <rss version="2.0">
            <channel>
                <title>Sample RSS Feed</title>
                <link>http://example.com</link>
                <description>This is a sample RSS feed</description>
                <item>
                    <title>Sample Article</title>
                    <pubDate>Mon, 01 Jan 2023 00:00:00 GMT</pubDate>
                    <link>http://example.com/sample-article</link>
                </item>
            </channel>
        </rss>
    """.trimIndent()

    val responseBody = ResponseBody.create(null, rssFeedWithMissingDescription)
    val articles = parseRssFeed(responseBody)

    assertEquals(1, articles.size)
    val article = articles[0]
    assertEquals("Sample Article", article.title)
    assertNull(article.description)
    assertEquals("Mon, 01 Jan 2023 00:00:00 GMT", article.pubDate)
    assertEquals("http://example.com/sample-article", article.link)
}

@Test
fun `should handle missing pubDate tag gracefully within an item`() {
    val rssFeedWithMissingPubDate = """
        <rss version="2.0">
            <channel>
                <title>Sample RSS Feed</title>
                <link>http://example.com</link>
                <description>This is a sample RSS feed</description>
                <item>
                    <title>Sample Article</title>
                    <description>This is a sample article description.</description>
                    <link>http://example.com/sample-article</link>
                </item>
            </channel>
        </rss>
    """.trimIndent()

    val responseBody = ResponseBody.create(null, rssFeedWithMissingPubDate)
    val articles = parseRssFeed(responseBody)

    assertEquals(1, articles.size)
    val article = articles[0]
    assertEquals("Sample Article", article.title)
    assertEquals("This is a sample article description.", article.description)
    assertNull(article.pubDate)
    assertEquals("http://example.com/sample-article", article.link)
}

@Test
fun `should handle missing link tag gracefully within an item`() {
    val rssFeedWithMissingLink = """
        <rss version="2.0">
            <channel>
                <title>Sample RSS Feed</title>
                <link>http://example.com</link>
                <description>This is a sample RSS feed</description>
                <item>
                    <title>Sample Article</title>
                    <description>This is a sample article description.</description>
                    <pubDate>Mon, 01 Jan 2023 00:00:00 GMT</pubDate>
                </item>
            </channel>
        </rss>
    """.trimIndent()

    val responseBody = ResponseBody.create(null, rssFeedWithMissingLink)
    val articles = parseRssFeed(responseBody)

    assertEquals(1, articles.size)
    val article = articles[0]
    assertEquals("Sample Article", article.title)
    assertEquals("This is a sample article description.", article.description)
    assertEquals("Mon, 01 Jan 2023 00:00:00 GMT", article.pubDate)
    assertNull(article.link)
}

@Test
fun `should correctly parse items with mixed case tag names`() {
    val mixedCaseRssFeed = """
        <rss version="2.0">
            <channel>
                <title>Sample RSS Feed</title>
                <link>http://example.com</link>
                <description>This is a sample RSS feed</description>
                <item>
                    <TITLE>Sample Article</TITLE>
                    <Description>This is a sample article description.</Description>
                    <PubDate>Mon, 01 Jan 2023 00:00:00 GMT</PubDate>
                    <LINK>http://example.com/sample-article</LINK>
                </item>
            </channel>
        </rss>
    """.trimIndent()

    val responseBody = ResponseBody.create(null, mixedCaseRssFeed)
    val articles = parseRssFeed(responseBody)

    assertEquals(1, articles.size)
    val article = articles[0]
    assertEquals("Sample Article", article.title)
    assertEquals("This is a sample article description.", article.description)
    assertEquals("Mon, 01 Jan 2023 00:00:00 GMT", article.pubDate)
    assertEquals("http://example.com/sample-article", article.link)
}
}
