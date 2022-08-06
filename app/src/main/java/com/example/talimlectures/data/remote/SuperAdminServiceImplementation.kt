package com.example.talimlectures.data.remote

import com.example.talimlectures.domain.dto.AdminModel
import com.example.talimlectures.domain.network.SuperAdminInterface
import com.example.talimlectures.util.Resource
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

class SuperAdminServiceImplementation(
    private val client: HttpClient
): SuperAdminInterface {
    override suspend fun createUser(adminModel: AdminModel): Resource<Unit> {
        return try {
            val response = client.post(SuperAdminInterface.EndPoints.createAdmin.url){
                contentType(ContentType.Application.Json)
                setBody(adminModel)
            }
            if (response.status == HttpStatusCode.OK){
                Resource.Success(Unit)
            } else{
                Resource.Error("UnknownError")

            }        } catch (e:Exception){
            Resource.Error(e.toString())
        }
    }

    override suspend fun deleteUser(username: String): Resource<Unit> {
      return  try {
           val response = client.delete("${ SuperAdminInterface.EndPoints.deleteAdmin.url}/$username")
           if (response.status == HttpStatusCode.OK){
               Resource.Success(Unit)
           } else{
               Resource.Error("UnknownError")
           }
            } catch (e:Exception){
           Resource.Error(e.toString())
            }
    }

    override suspend fun getAllAdmins(): Resource<List<AdminModel>> {
        return  try {
            val response = client.get( SuperAdminInterface.EndPoints.getAllAdmins.url)
            if (response.status == HttpStatusCode.OK){
                Resource.Success(
                    data = response.body()
                )
            } else{
                Resource.Error("UnknownError")
            }
        } catch (e:Exception){
            Resource.Error(e.toString())
        }
    }

    override suspend fun deleteAllAdmins(): Resource<Unit> {
        return  try {
            val response = client.get( SuperAdminInterface.EndPoints.deleteAllAdmins.url)
            if (response.status == HttpStatusCode.OK){
                Resource.Success(Unit)
            } else{
                Resource.Error("UnknownError")
            }
        } catch (e:Exception){
            Resource.Error(e.toString())
        }
    }

    override suspend fun deleteAllLecture(): Resource<Unit> {
        return  try {
            val response = client.get( SuperAdminInterface.EndPoints.deleteAllLecture.url)
            if (response.status == HttpStatusCode.OK){
                Resource.Success(Unit)
            } else{
                Resource.Error("UnknownError")
            }
        } catch (e:Exception){
            Resource.Error(e.toString())
        }
    }

    override suspend fun deleteAllCategory(): Resource<Unit> {
        return  try {
            val response = client.get( SuperAdminInterface.EndPoints.deleteAllCategory.url)
            if (response.status == HttpStatusCode.OK){
                Resource.Success(Unit)
            } else{
                Resource.Error("UnknownError")
            }
        } catch (e:Exception){
            Resource.Error(e.toString())
        }
    }
}