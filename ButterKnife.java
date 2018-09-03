	implementation 'com.jakewharton:butterknife:8.8.1'
    annotationProcessor 'com.jakewharton:butterknife-compiler:8.8.1'
---------------------------------------------------------------------------------------------
1st For Fragments:-
{
	Unbinder unbinder;
	
	@BindView(R.id.editText_login_password)
    EditText ediText_login_password;
	
	-------------------
	 @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.home_page_fragment, container, false);
        unbinder = ButterKnife.bind(this,view);
	
		return view;
		}
		
	@Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }
	
	//Single View Click
	 @OnClick(R.id.fab_homepage_add)
    public void onSettingsClick() {
		/*do as you want */
		ediText_login_password.requestFocus();
		}
	
	//Multiple View Click
	 @OnClick({R.id.cardView_addClient,R.id.cardView_addScheme})
    public void onCardClick(View view){
        switch (view.getId()){
            case R.id.cardView_addClient:
                /*do as you want */
                break;

            case R.id.cardView_addScheme:
                /*do as you want */
                break;
            
        }
    }
}

2nd. For Activity:-
{
	 @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);

    }
}
