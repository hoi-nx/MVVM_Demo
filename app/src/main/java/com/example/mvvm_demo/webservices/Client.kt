
import com.example.mvvm_demo.webservices.BaseRestClient
import com.example.mvvm_demo.webservices.IServices


object Client : BaseRestClient() {
    val service: IServices by lazy {
        createService("https://api.github.com",IServices::class.java)
    }
}