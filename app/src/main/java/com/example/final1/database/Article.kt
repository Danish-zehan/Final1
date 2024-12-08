@Entity(tableName = "articles")
data class Article(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String,
    val pubDate: String,
    val link: String
)