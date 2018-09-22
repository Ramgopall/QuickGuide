1st Create a dialogFragment:-
{**
public class ConfirmationFragmentDialog extends DialogFragment {

    View view;

    Button backbutton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_confirmation_fragment_dialog, container, false);

        
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().cancel();
            }
        });

    return view;
	}

}

**}

------------------------------------------------------------------------------------

2nd How to call it:-
{**
	 FragmentManager manager = getFragmentManager();
     Fragment frag = manager.findFragmentByTag("ConfirmationFragmentDialog");
     if (frag != null) {
         manager.beginTransaction().remove(frag).commit();
     }
     ConfirmationFragmentDialog fragmentDialog = new ConfirmationFragmentDialog();
	 fragmentDialog.show(manager, "ConfirmationFragmentDialog");

**}
