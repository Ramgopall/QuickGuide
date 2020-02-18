package com.duke.diskoverusosyal.retrofitData

import com.duke.diskoverusosyal.model.CheckReferCodeModel
import com.duke.diskoverusosyal.model.CommonResponse.CommonResponseModel
import com.duke.diskoverusosyal.model.VersionCheckModel
import com.duke.diskoverusosyal.model.addComment.AddCommentModel
import com.duke.diskoverusosyal.model.bankInfo.BankInfoModel
import com.duke.diskoverusosyal.model.chatHistory.ChatHistoryModel
import com.duke.diskoverusosyal.model.chatUsers.ChatUsersModel
import com.duke.diskoverusosyal.model.comments.CommentsModel
import com.duke.diskoverusosyal.model.forgot.ForgotPasswordModel
import com.duke.diskoverusosyal.model.home.HomeModel
import com.duke.diskoverusosyal.model.likeList.LikeListModel
import com.duke.diskoverusosyal.model.login.LoginModel
import com.duke.diskoverusosyal.model.notification.NotificationModel
import com.duke.diskoverusosyal.model.offer.OfferModel
import com.duke.diskoverusosyal.model.otp_verify.OtpVerifyModel
import com.duke.diskoverusosyal.model.packageList.PackageListModel
import com.duke.diskoverusosyal.model.paymentToken.PayMentTokenModel
import com.duke.diskoverusosyal.model.refer.ReferModel
import com.duke.diskoverusosyal.model.register.RegisterModel
import com.duke.diskoverusosyal.model.search.SearchModel
import com.duke.diskoverusosyal.model.sendOtp.SendOtpModel
import com.duke.diskoverusosyal.model.singlePost.SinglePostModel
import com.duke.diskoverusosyal.model.updateNumber.UpdateNumberModel
import com.duke.diskoverusosyal.model.updateProfile.UpdateProfileModel
import com.duke.diskoverusosyal.model.updateToken.UpdateTokenModel
import com.duke.diskoverusosyal.model.userPosts.UserPostsModel
import com.duke.diskoverusosyal.model.userProfile.UserProfileModel
import com.duke.diskoverusosyal.model.wallet.WalletModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {

    @FormUrlEncoded
    @POST("clogin.php")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginModel>

    @FormUrlEncoded
    @POST("forgot.php")
    fun forgot(
        @Field("email") email: String
    ): Call<ForgotPasswordModel>

    @FormUrlEncoded
    @POST("sociallogin.php")
    fun socialLogin(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("photo") photo: String,
        @Field("uid") uniqueId: String,
        @Field("type") type: String,
        @Field("wtype") wtype: String
    ): Call<LoginModel>

//    @Multipart
//    @POST("registration.php")
//    fun register(
//        @Part("name") name: RequestBody,
//        @Part("email") email: RequestBody,
//        @Part("phone") phone: RequestBody,
//        @Part("reffrel") reffrel: RequestBody,
//        @Part("password") password: RequestBody,
//        @Part("gender") gender: RequestBody,
//        @Part("ccode") ccode: RequestBody,
//        @Part file: MultipartBody.Part
//    ): Call<RegisterModel>

    @Multipart
    @POST("registration.php")
    fun register(
        @Part("name") name: RequestBody,
        @Part("email") email: RequestBody,
        @Part("phone") phone: RequestBody,
        @Part("reffrel") reffrel: RequestBody,
        @Part("password") password: RequestBody,
        @Part("gender") gender: RequestBody,
        @Part("ccode") ccode: RequestBody,
        @Part("wtype") type: RequestBody,
        @Part file: MultipartBody.Part
    ): Call<RegisterModel>

    @FormUrlEncoded
    @POST("reotp.php")
    fun resendOtp(
        @Field("sess") id: String
    ): Call<OtpVerifyModel>

    @FormUrlEncoded
    @POST("motp.php")
    fun verifyOtp(
        @Field("sess") id: String,
        @Field("otp") otp: String
    ): Call<OtpVerifyModel>

    @FormUrlEncoded
    @POST("motp.php")
    fun emailVerify(
        @Field("sess") id: String,
        @Field("otp") otp: String
    ): Call<OtpVerifyModel>

    @Multipart
    @POST("updatepro.php")
    fun updateProfile(
        @Part("cid") cid: RequestBody,
        @Part("name") name: RequestBody,
        @Part("gender") gender: RequestBody,
        @Part("exp") exp: RequestBody,
        @Part("detail") detail: RequestBody,
        @Part("skill") skill: RequestBody,
        @Part file: MultipartBody.Part
    ): Call<UpdateProfileModel>

    @FormUrlEncoded
    @POST("sendotp.php")
    fun sendOtp(
        @Field("otp") otp: String?,
        @Field("phone") phone: String?
    ): Call<SendOtpModel>

    @FormUrlEncoded
    @POST("updatenumber.php")
    fun updateNumber(
        @Field("sess") sess: String?,
        @Field("phone") number: String?
    ): Call<UpdateNumberModel>

    @FormUrlEncoded
    @POST("sos_fcmtoken.php")
    fun updateToken(
        @Field("sess") sess: String?,
        @Field("fcmtoken") fcmtoken: String?
    ): Call<UpdateTokenModel>

    ///////////////////////////////////////////////////////////////////////////////////////////////

    @FormUrlEncoded
    @POST("sos_check.php")
    fun isVideoUploadable(
        @Field("sess") sess: String?
    ): Call<CommonResponseModel>

    @Multipart
    @POST("sos_upload.php")
    fun uploadPost(
        @Part("sess") sess: RequestBody,
        @Part("type") type: RequestBody,
        @Part("desc") desc: RequestBody,
        @Part file: MultipartBody.Part
    ): Call<CommonResponseModel>

    @GET("sos_package.php")
    fun getPackage(): Call<PackageListModel>

    @FormUrlEncoded
    @POST("sos_profile.php")
    fun getUserProfile(
        @Field("sess") sess: String?,
        @Field("mysess") mysess: String?
    ): Call<UserProfileModel>

    @FormUrlEncoded
    @POST("sos_profile_posts.php")
    fun getUserPosts(
        @Field("sess") sess: String?
    ): Call<UserPostsModel>

    @FormUrlEncoded
    @POST("sos_post.php")
    fun getSinglePosts(
        @Field("id") id: String?,
        @Field("sess") sess: String?
    ): Call<SinglePostModel>

    @FormUrlEncoded
    @POST("sos_postlike.php")
    fun getLikeList(
        @Field("id") id: String?
    ): Call<LikeListModel>

    @FormUrlEncoded
    @POST("sos_like.php")
    fun likePost(
        @Field("pid") id: String?,
        @Field("sess") sess: String?,
        @Field("like") like: String?
    ): Call<CommonResponseModel>

    @FormUrlEncoded
    @POST("sos_delete.php")
    fun deletePost(
        @Field("id") id: String?,
        @Field("sess") sess: String?
    ): Call<CommonResponseModel>

    @FormUrlEncoded
    @POST("sos_wallet.php")
    fun getWallet(
        @Field("sess") sess: String?
    ): Call<WalletModel>

    @FormUrlEncoded
    @POST("sos_notification.php")
    fun getNotification(
        @Field("sess") sess: String?
    ): Call<NotificationModel>

    @FormUrlEncoded
    @POST("sos_reffrel.php")
    fun getRefer(
        @Field("sess") sess: String?
    ): Call<ReferModel>

    @FormUrlEncoded
    @POST("sos_seen.php")
    fun markReadedNotification(
        @Field("sess") sess: String?
    ): Call<CommonResponseModel>

    @FormUrlEncoded
    @POST("sess.php")
    fun isUserBlocked(
        @Field("sess") sess: String?
    ): Call<CommonResponseModel>

    @FormUrlEncoded
    @POST("sos_bank.php")
    fun getBankInfo(
        @Field("sess") sess: String?
    ): Call<BankInfoModel>

    @FormUrlEncoded
    @POST("sos_bankinsert.php")
    fun requestWithdraw(
        @Field("name") name: String?,
        @Field("bname") bname: String?,
        @Field("accountno") accountno: String?,
        @Field("ifsc") ifsc: String?,
        @Field("amount") amount: String?,
        @Field("sess") sess: String?
    ): Call<CommonResponseModel>

    @FormUrlEncoded
    @POST("sos_token.php")
    fun getPaymentToken(
        @Field("sess") sess: String?,
        @Field("id") id: String?
    ): Call<PayMentTokenModel>

    @FormUrlEncoded
    @POST("sos_payment_done.php")
    fun submitTransectionDonateion(
        @Field("type") type: String?,
        @Field("orderid") orderId: String?,
        @Field("paymentid") paymentid: String?,
        @Field("signature") signature: String?
    ): Call<CommonResponseModel>

    @FormUrlEncoded
    @POST("sos_homepage.php")
    fun getHomeData(
        @Field("sess") sess: String?,
        @Field("page") page: Int?
    ): Call<HomeModel>

    @FormUrlEncoded
    @POST("sos_offernotification.php")
    fun getOfferDetail(
        @Field("id") id: String?
    ): Call<OfferModel>

    @FormUrlEncoded
    @POST("sos_search.php")
    fun getSearchResult(
        @Field("keyword") keyword: String?
    ): Call<SearchModel>

    @FormUrlEncoded
    @POST("sos_ref.php")
    fun checkReferCode(
        @Field("sess") sess: String?
    ): Call<CheckReferCodeModel>

    @FormUrlEncoded
    @POST("sos_ref_insert.php")
    fun insertReferCode(
        @Field("sess") sess: String?,
        @Field("wtype") wtype: String?,
        @Field("ref") ref: String?
    ): Call<CommonResponseModel>

    @FormUrlEncoded
    @POST("sos_deactive.php")
    fun deactive(
        @Field("sess") sess: String?
    ): Call<CommonResponseModel>

    @FormUrlEncoded
    @POST("sos_parmanentdelete.php")
    fun deleteAccount(
        @Field("sess") sess: String?
    ): Call<CommonResponseModel>

    @FormUrlEncoded
    @POST("sos_invitation.php")
    fun inviteFriend(
        @Field("r_sess") r_sess: String?,
        @Field("s_sess") s_sess: String?,
        @Field("status") status: String?
    ): Call<CommonResponseModel>

    @FormUrlEncoded
    @POST("sos_invitation_reply.php")
    fun inviteReply(
        @Field("sess") sess: String?,
        @Field("pid") pid: String?,
        @Field("nid") nid: String?,
        @Field("status") status: String?
    ): Call<CommonResponseModel>

    @FormUrlEncoded
    @POST("sos_chat_users.php")
    fun getChatUsers(
        @Field("sess") sess: String?
    ): Call<ChatUsersModel>

    @FormUrlEncoded
    @POST("sos_block.php")
    fun blockFriend(
        @Field("sess") sess: String?,
        @Field("mysess") mysess: String?,
        @Field("block") block: String?
    ): Call<CommonResponseModel>

    @FormUrlEncoded
    @POST("sos_chat_history.php")
    fun getChatHistory(
        @Field("myid") myid: String?,
        @Field("toid") toid: String?
    ): Call<ChatHistoryModel>

    @FormUrlEncoded
    @POST("sos_coment.php")
    fun commentOnPost(
        @Field("sess") sess: String?,
        @Field("coment") coment: String?,
        @Field("pid") pid: String?
    ): Call<AddCommentModel>

    @FormUrlEncoded
    @POST("sos_postcoment.php")
    fun getComments(
        @Field("id") postId: String?
    ): Call<CommentsModel>

    @FormUrlEncoded
    @POST("sos_cdelete.php")
    fun deleteComment(
        @Field("id") commentId: String?,
        @Field("pid") postId: String?
    ): Call<CommonResponseModel>

    @FormUrlEncoded
    @POST("appvcheck.php")
    fun checkAppVersion(
        @Field("sess") sess: String?,
        @Field("version") version: String?
    ): Call<VersionCheckModel>

}