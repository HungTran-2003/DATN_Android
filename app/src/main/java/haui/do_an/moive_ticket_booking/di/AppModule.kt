package haui.do_an.moive_ticket_booking.di

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import haui.do_an.moive_ticket_booking.api.ApiRoutes
import haui.do_an.moive_ticket_booking.api.AuthInterceptor
import haui.do_an.moive_ticket_booking.api.CinemaApiService
import haui.do_an.moive_ticket_booking.api.CouponService
import haui.do_an.moive_ticket_booking.api.MoiveAPIService
import haui.do_an.moive_ticket_booking.api.OtpAPIService
import haui.do_an.moive_ticket_booking.api.PayOsService
import haui.do_an.moive_ticket_booking.api.ShowTimeApiService
import haui.do_an.moive_ticket_booking.api.TicketApiService
import haui.do_an.moive_ticket_booking.api.TmdbAPIService
import haui.do_an.moive_ticket_booking.api.UserAPIService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // --- Qualifiers ---
    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class LocalRetrofit

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class TMDbRetrofit

    // --- Logging Interceptor ---
    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    // --- OkHttp cho Local ---
    @Provides
    @Singleton
    @LocalRetrofit
    fun provideLocalOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        authInterceptor: AuthInterceptor // Inject AuthInterceptor ở đây
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor) // Thêm AuthInterceptor
            .addInterceptor(httpLoggingInterceptor)
            .build()
    }

    // --- OkHttp cho TMDb ---
    @Provides
    @Singleton
    @TMDbRetrofit
    fun provideTmdbOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build()
    }

    // --- Retrofit cho Local ---
    @Provides
    @Singleton
    @LocalRetrofit
    fun provideLocalRetrofit(@LocalRetrofit okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(ApiRoutes.BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    // --- Retrofit cho TMDb ---
    @Provides
    @Singleton
    @TMDbRetrofit
    fun provideTmdbRetrofit(@TMDbRetrofit okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    // --- API Service cho server nội bộ ---
    @Provides
    @Singleton
    fun provideCinemaApiService(@LocalRetrofit retrofit: Retrofit): CinemaApiService {
        return retrofit.create(CinemaApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideOtpApiService(@LocalRetrofit retrofit: Retrofit): OtpAPIService {
        return retrofit.create(OtpAPIService::class.java)
    }

    @Provides
    @Singleton
    fun provideUserApiService(@LocalRetrofit retrofit: Retrofit): UserAPIService {
        return retrofit.create(UserAPIService::class.java)
    }

    // --- API Service cho TMDb ---
    @Provides
    @Singleton
    fun provideTmdbApiService(@TMDbRetrofit retrofit: Retrofit): TmdbAPIService {
        return retrofit.create(TmdbAPIService::class.java)
    }

    @Provides
    @Singleton
    fun provideMovieApiService(@LocalRetrofit retrofit: Retrofit): MoiveAPIService {
        return retrofit.create(MoiveAPIService::class.java)
    }

    @Provides
    @Singleton
    fun provideAuthInterceptor(sharedPreferences: SharedPreferences): AuthInterceptor {
        return AuthInterceptor(sharedPreferences)
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideShowTimeApiService(@LocalRetrofit retrofit: Retrofit): ShowTimeApiService {
        return retrofit.create(ShowTimeApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideTicketApiService(@LocalRetrofit retrofit: Retrofit): TicketApiService {
        return retrofit.create(TicketApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideBookingApiService(@LocalRetrofit retrofit: Retrofit): haui.do_an.moive_ticket_booking.api.
            BookingApiService {
        return retrofit.create(haui.do_an.moive_ticket_booking.api.BookingApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideCouponApiService(@LocalRetrofit retrofit: Retrofit): CouponService {
        return retrofit.create(CouponService::class.java)
    }

    @Provides
    @Singleton
    fun providePayOsApiService(@LocalRetrofit retrofit: Retrofit): PayOsService {
        return retrofit.create(PayOsService::class.java)
    }



}