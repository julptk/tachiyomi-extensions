package eu.kanade.tachiyomi.extension.id.customextension

import eu.kanade.tachiyomi.source.Source
import eu.kanade.tachiyomi.source.online.HttpSource
import okhttp3.Request
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

class CustomExtension : HttpSource() {
    override val name = "Custom Extension"
    override val baseUrl = "https://example.com"
    override val lang = "id"
    override val supportsLatest = true

    override fun popularMangaRequest(page: Int): Request {
        // Request untuk mendapatkan daftar manga populer
        return Request.Builder()
            .url("$baseUrl/popular?page=$page")
            .build()
    }

    override fun popularMangaParse(response: Response): List<Manga> {
        // Parsing daftar manga populer dari response
        val document = response.asJsoup()
        val mangaElements = document.select("div.manga-item") // Ganti sesuai elemen yang sesuai

        return mangaElements.map {
            val title = it.select("h3.title").text()
            val url = it.select("a").attr("href")
            val imageUrl = it.select("img").attr("src")
            Manga.create(title, url, imageUrl)
        }
    }

    override fun latestUpdatesRequest(page: Int): Request {
        // Request untuk mendapatkan manga terbaru
        return Request.Builder()
            .url("$baseUrl/latest?page=$page")
            .build()
    }

    override fun latestUpdatesParse(response: Response): List<Manga> {
        // Parsing manga terbaru
        val document = response.asJsoup()
        val mangaElements = document.select("div.manga-item") // Ganti sesuai elemen yang sesuai

        return mangaElements.map {
            val title = it.select("h3.title").text()
            val url = it.select("a").attr("href")
            val imageUrl = it.select("img").attr("src")
            Manga.create(title, url, imageUrl)
        }
    }

    override fun searchMangaRequest(page: Int, query: String, filters: List<Filter>): Request {
        // Request pencarian manga
        return Request.Builder()
            .url("$baseUrl/search?q=$query&page=$page")
            .build()
    }

    override fun searchMangaParse(response: Response): List<Manga> {
        // Parsing hasil pencarian
        val document = response.asJsoup()
        val mangaElements = document.select("div.manga-item") // Ganti sesuai elemen yang sesuai

        return mangaElements.map {
            val title = it.select("h3.title").text()
            val url = it.select("a").attr("href")
            val imageUrl = it.select("img").attr("src")
            Manga.create(title, url, imageUrl)
        }
    }

    override fun mangaDetailsRequest(manga: Manga): Request {
        // Request untuk mendapatkan detail manga
        return Request.Builder()
            .url(manga.url)
            .build()
    }

    override fun mangaDetailsParse(response: Response): MangaDetails {
        // Parsing detail manga
        val document = response.asJsoup()
        val description = document.select("div.description").text()
        val genres = document.select("div.genres a").map { it.text() }
        val status = document.select("div.status").text()

        return MangaDetails(description, genres, status)
    }

    override fun chapterListRequest(manga: Manga): Request {
        // Request untuk mendapatkan daftar chapter manga
        return Request.Builder()
            .url(manga.url)
            .build()
    }

    override fun chapterListParse(response: Response): List<Chapter> {
        // Parsing daftar chapter
        val document = response.asJsoup()
        val chapterElements = document.select("div.chapter-item") // Ganti sesuai elemen yang sesuai

        return chapterElements.map {
            val title = it.select("h3.chapter-title").text()
            val url = it.select("a").attr("href")
            val date = it.select("span.date").text()

            Chapter.create(title, url, date)
        }
    }

    override fun pageListRequest(chapter: Chapter): Request {
        // Request untuk mendapatkan daftar halaman chapter
        return Request.Builder()
            .url(chapter.url)
            .build()
    }

    override fun pageListParse(response: Response): List<Page> {
        // Parsing daftar halaman chapter
        val document = response.asJsoup()
        val pageElements = document.select("img.page-img") // Ganti sesuai elemen yang sesuai

        return pageElements.mapIndexed { index, element ->
            Page(index, element.attr("src"))
        }
    }

    // Fungsi tambahan untuk menyesuaikan atau memodifikasi request dan response jika diperlukan
}
