class Application : Application(), KodeinAware {

    override val kodein = Kodein.lazy {
        import(androidXModule(this@TrivialLanguagesApplication))

        bind() from singleton { NetworkConnectionInterceptor(instance()) }
        bind() from singleton { FirebaseSource(instance()) }
        bind() from singleton { AppDatabase(instance()) }
        bind() from singleton { MyApi(instance(),instance()) }
        bind() from singleton { UserRepository(instance(), instance()) }
        bind() from singleton { HomeViewModelFactory(instance()) }
        bind() from singleton { QuestionRepository(instance(), instance()) }
        bind() from provider { LoginViewModelFactory(instance(),instance()) }
        bind() from provider { QuestionViewModelFactory(instance()) }

    }

}