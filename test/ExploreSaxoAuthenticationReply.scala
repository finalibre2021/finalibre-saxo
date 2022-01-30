import play.api.libs.json.JsValue.jsValueToJsLookup
import play.api.libs.json.Json

object ExploreSaxoAuthenticationReply {
  def main(args: Array[String]): Unit = {
    val code = "a41b25ea-62b1-455a-a3b2-855db81cf91f"
    val state = "8bEnZuH%2folb%2b16VATnqJrVzZK2SkYXuQDmGQk%2bIZNYFMhUgZsjyVA%2fmK1r2uJOG%2fqUl66kdVA24moF68FEDvM2sotslP4RaYdHcZj0tWyUaWBHbznmQXy4UcLh0Bi2PfjnpdF0LtU4QN2CRUbGrJsmNNIKfUlxSZ%2bqsi8F6rBQvfm94GdW5KAaIztxuGcxfQ]"
    val replyBody = """{"access_token":"eyJhbGciOiJFUzI1NiIsIng1dCI6IkRFNDc0QUQ1Q0NGRUFFRTlDRThCRDQ3ODlFRTZDOTEyRjVCM0UzOTQifQ.eyJvYWEiOiI3Nzc3MCIsImlzcyI6Im9hIiwiYWlkIjoiMjUwNyIsInVpZCI6InNGd2sySUd8cDIybzl2bUIyR0Vnb0E9PSIsImNpZCI6InNGd2sySUd8cDIybzl2bUIyR0Vnb0E9PSIsImlzYSI6IkZhbHNlIiwidGlkIjoiNzUzMyIsInNpZCI6IjQ4ZjdmOWViNGEzNDQ0NDI4ZDgyNDM1OWI2MzFmZDM5IiwiZGdpIjoiODQiLCJleHAiOiIxNjQzNTU3NjE2Iiwib2FsIjoiMUYifQ.KA8KEvkUzDMczV_s9zED2VY0VCsk48LBBDc38VCEhH714ddqT_zfYpMRdsdFs3y7IjboC_OVw_498jIIPLsm3w","token_type":"Bearer","expires_in":1200,"refresh_token":"66dc0857-e5ed-4041-af03-78cd2595299b","refresh_token_expires_in":3600,"base_uri":null}"""
    val json = Json.parse(replyBody)
    val accessToken = (json \ "access_token").as[String]
    val expiresIn = (json \ "expires_in").as[Int]
    val refreshAccessToken = (json \ "refresh_token").as[String]
    val refreshExpiresIn = (json \ "refresh_token_expires_in").as[Int]

    println(s"Access token: $accessToken")
    println(s"Expires in: $expiresIn")
    println(s"Refresh token: $refreshAccessToken")
    println(s"Refresh token expires in: $refreshExpiresIn")


  }



}
