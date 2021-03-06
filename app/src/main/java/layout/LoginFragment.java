package layout;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import org.protaxiandroidapp.R;
import org.protaxiandroidapp.restful.RequestVolley;
import java.util.HashMap;
import java.util.Map;

public class LoginFragment extends android.app.Fragment implements View.OnClickListener {

    Button btnLogin;
    Button btnCreateAccount;
    EditText txtEmail;
    EditText txtPassword;
    View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        rootView = view;

        btnLogin = (Button) view.findViewById(R.id.email_sign_in_button);
        btnLogin.setOnClickListener(this);
        btnCreateAccount = (Button) view.findViewById(R.id.btnCreateAccount);
        btnCreateAccount.setOnClickListener(this);
        txtEmail = (EditText)view.findViewById(R.id.email);
        txtPassword = (EditText)view.findViewById(R.id.password);

        txtEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (txtEmail.getText().toString().trim().isEmpty()) {
                    txtEmail.setError("El email es obligatorio");
                    return;
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(txtEmail.getText().toString().trim()).matches()) {
                    txtEmail.setError("El email no tiene el formato correcto");
                    return;
                }
            }
        });

        txtPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (txtPassword.getText().toString().trim().isEmpty()) {
                    txtPassword.setError("La contraseña es obligatorio");
                }
            }
        });

        return view;
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.email_sign_in_button:
                if (validateForm())
                    login(v);
                break;

            case R.id.btnCreateAccount:
                createAccount(v);
                break;
        }
    }

    private void createAccount(View v) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.login_fragment, new CreateAccountFragment(), "fragment_screen");
        ft.commit();
    }

    private boolean validateForm() {
        boolean isValid = true;

        if (!Patterns.EMAIL_ADDRESS.matcher(txtEmail.getText().toString().trim()).matches()) {
            txtEmail.setError("El email no tiene el formato correcto");
            isValid = false;
        }

        if (txtEmail.getText().toString().trim().isEmpty()) {
            txtEmail.setError("El email es obligatorio");
            isValid = false;
        }

        if (txtPassword.getText().toString().trim().isEmpty()) {
            txtPassword.setError("La contraseña es obligatoria");
            isValid = false;
        }

        return isValid;
    }

    private void login(View v) {
        try {

            RequestVolley requestVolley = new RequestVolley(this.getActivity());

            Map<String, String> params = new HashMap<>();
            params.put("email", txtEmail.getText().toString());
            params.put("password", txtPassword.getText().toString());

            requestVolley.post(Constants.urlBase, Constants.urlSpecificLogin, params);

        } catch (Exception e) {
            General.showAlert(v, "Ocurrió un problema", "Login");
        }
    }

}
