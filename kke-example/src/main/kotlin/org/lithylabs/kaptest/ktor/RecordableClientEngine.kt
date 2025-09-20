//package org.rela.test_recorder.ktor
//
//import io.ktor.client.engine.mock.*
//import io.ktor.http.*
//import io.ktor.utils.io.*
//
//class RecordableClientEngine: MockEngine(MockEngineConfig().apply{
//
//})
//
//val mockEngine = MockEngine { request ->
//    respond(
//        content = ByteReadChannel("""{"ip":"127.0.0.1"}"""),
//        status = HttpStatusCode.OK,
//        headers = headersOf(HttpHeaders.ContentType, "application/json")
//    )
//}