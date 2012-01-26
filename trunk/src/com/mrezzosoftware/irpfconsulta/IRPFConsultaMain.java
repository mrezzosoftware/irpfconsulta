package com.mrezzosoftware.irpfconsulta;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class IRPFConsultaMain extends Activity {
	
	private EditText txtCpf;
	private EditText txtCaptcha;
	private ImageButton btConsultar;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.irpfconsultamain);
        btConsultar = (ImageButton) findViewById(R.id.btnConsultar);
        btConsultar.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				txtCpf = (EditText) findViewById(R.id.txtCpf);
				txtCaptcha = (EditText) findViewById(R.id.txtCaptcha);
				
				Toast.makeText(IRPFConsultaMain.this,
						String.format("CPF: %s \nCaptcha: %s", txtCpf.getText().toString(), txtCaptcha.getText().toString()),
						Toast.LENGTH_SHORT).show();
			}
		});
    }
}