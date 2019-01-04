package fr.zabricraft.delta;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.calculate:
                calculate();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void calculate() {
        TextView result = findViewById(R.id.label_result);
        EditText a2 = findViewById(R.id.value_a);
        EditText b2 = findViewById(R.id.value_b);
        EditText c2 = findViewById(R.id.value_c);
        try {
            double a = Double.parseDouble(a2.getText().toString().replaceAll(",", "."));
            double b = Double.parseDouble(b2.getText().toString().replaceAll(",", "."));
            double c = Double.parseDouble(c2.getText().toString().replaceAll(",", "."));
            if(a != 0) {
                String build = "Forme développée : "+a+"x²"+ns(b)+"x"+ns(c);
                build += "\nForme canonique : "+a+"(x"+ns(b/(2*a))+")²"+ns((-Math.pow(b, 2)+4*a*c)/(4*a))+")";
                double d = Math.pow(b, 2)-(4*a*c);
                build += "\n\n∆ = "+d;
                if(d > 0){
                    double x1 = (-b-Math.pow(d, 0.5))/(2*a);
                    double x2 = (-b+Math.pow(d, 0.5))/(2*a);
                    build += "\nx1 = "+x1+"\nx2 = "+x2+"\nForme factorisée : "+a+"(x"+ns(-x1)+")(x"+ns(-x2)+")";
                }else if(d == 0){
                    double x0 = -b/(2*a);
                    build += "\nx0 = "+x0+"\nForme factorisée comme forme canonique";
                }else{
                    build += "\nPas de racine\nPas de forme factorisée";
                }
                result.setText(build);
            }else{
                result.setText("a = 0, problème du premier degré !");
            }
        } catch (NumberFormatException e) {
            result.setText("Entrez des valeurs numériques dans toutes les cases !");
        }
    }

    public String ns(double d){
        return (d >= 0 ? "+" : "")+d;
    }
}
