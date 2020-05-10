package com.mnaufalazwar.sibandarapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mnaufalazwar.sibandarapp.R;
import com.mnaufalazwar.sibandarapp.adapter.PaymentDetailAdapter;
import com.mnaufalazwar.sibandarapp.common.CommonEndpoint;
import com.mnaufalazwar.sibandarapp.model.DataTransactionModel;
import com.mnaufalazwar.sibandarapp.model.PaymentModel;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class PaymentDetailToPayActivity extends AppCompatActivity {

    public static final int REQUEST_PAYMENT = 301;
    public static final int RESULT_PAYMENT = 302;
    public static final String EXTRA_DATA_PAYMENT_TYPE = "EXTRA_DATA_PAYMENT_TYPE";
    public static final String EXTRA_DATA_PAYMENT_IN = "EXTRA_DATA_PAYMENT_IN";
    public static final String EXTRA_DATA_PAYMENT_IN_POS = "EXTRA_DATA_PAYMENT_IN_POS";
    public static final String EXTRA_DATA_PAYMENT_OUT = "EXTRA_DATA_PAYMENT_OUT";
    public static final String EXTRA_DATA_PAYMENT_POS = "EXTRA_DATA_PAYMENT_POS";

    private static String payType;

    RecyclerView rvPaymentDetail;
    EditText etAmountPayment;
    Button btnPay;

    PaymentDetailAdapter paymentDetailAdapter;
    PaymentModel thisPaymentModel = new PaymentModel();
    PaymentModel paymentModelOut = new PaymentModel();
    ArrayList<DataTransactionModel> thisDataTransactionModels = new ArrayList<>();
    ArrayList<DataTransactionModel> dataTransactionModelsOut = new ArrayList<>();
    int pos = -1;
    String subjectId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_detail_to_pay);

        btnPay = findViewById(R.id.btn_pay);
        etAmountPayment = findViewById(R.id.et_amount_payment);
        rvPaymentDetail = findViewById(R.id.rv_payment_detail);

        if(getIntent().getParcelableExtra(EXTRA_DATA_PAYMENT_IN) != null){

            thisPaymentModel = getIntent().getParcelableExtra(EXTRA_DATA_PAYMENT_IN);
            thisDataTransactionModels = thisPaymentModel.getDataTransactionModels();
            payType = getIntent().getStringExtra(EXTRA_DATA_PAYMENT_TYPE);

            Log.d("EXTRA SUBJECT", "subject: " + thisPaymentModel.getSubject());
            Log.d("EXTRA_DATA_PAYMENT SIZE", "" + thisDataTransactionModels.size());

            pos = getIntent().getIntExtra(EXTRA_DATA_PAYMENT_IN_POS, -1);
            subjectId = thisPaymentModel.getSubject();
        }

        paymentModelOut = thisPaymentModel;

        rvPaymentDetail.setLayoutManager(new LinearLayoutManager(PaymentDetailToPayActivity.this));
        rvPaymentDetail.setHasFixedSize(true);

        paymentDetailAdapter = new PaymentDetailAdapter(PaymentDetailToPayActivity.this);
        paymentDetailAdapter.setList(thisDataTransactionModels);
        paymentDetailAdapter.setOnItemCeckCallback(new PaymentDetailAdapter.OnItemCheckCallback() {
            @Override
            public void onItemChecked(DataTransactionModel data, int position, boolean isChecked) {

//                paymentModelOut.getDataTransactionModels().remove(position);
                paymentModelOut.getDataTransactionModels().get(position).setPaid(isChecked);
            }
        });

        rvPaymentDetail.setAdapter(paymentDetailAdapter);

        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(etAmountPayment.getText().toString().isEmpty()
                || etAmountPayment.getText().toString().trim().equals("")){

                    Toast.makeText(PaymentDetailToPayActivity.this, "Nominal pembayaran tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                }
                else {

                    String subjectId = paymentModelOut.getSubject();
                    String paid = etAmountPayment.getText().toString().trim();
                    String cardsForBody = "[";

                    for(int i = 0 ; i < paymentModelOut.getDataTransactionModels().size() ; i ++){
                        if(paymentModelOut.getDataTransactionModels().get(i).isPaid()){

                            cardsForBody += "{\"_id\":";
                            cardsForBody += ("\"" + paymentModelOut.getDataTransactionModels().get(i).getTransactionCode() + "\"},");

                            paymentModelOut.getDataTransactionModels().remove(i);
                        }
                    }

                    if(cardsForBody.length() > 1){
                        cardsForBody = cardsForBody.substring(0, (cardsForBody.length() - 1));
                    }
                    cardsForBody += "]";

                    String bodyPostPay = generateBodyPostPay("testing01", subjectId, paid, cardsForBody);

                    Log.d("bodyPostPay", bodyPostPay);

                    sendCardsToPay(bodyPostPay);

                    Intent intent = new Intent();
                    intent.putExtra(EXTRA_DATA_PAYMENT_OUT, paymentModelOut);
                    intent.putExtra(EXTRA_DATA_PAYMENT_POS, pos);
                    setResult(RESULT_PAYMENT, intent);
                    finish();
                }
            }
        });
    }

    private void sendCardsToPay(String jsonString) {

        try {

            final JSONObject jsonObject = new JSONObject(jsonString);

            Log.d("JSON KIRIM::", jsonObject.toString());

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try{

                        String endpoint = "http://" + CommonEndpoint.IP + ":" + CommonEndpoint.PORT + "/payments/addpayment";

//                        URL url = new URL("http://192.168.100.78:8080/payments/addpayment");

                        URL url = new URL(endpoint);
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setRequestMethod("POST");
                        conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                        conn.setRequestProperty("Accept","application/json");
                        conn.setDoOutput(true);
                        conn.setDoInput(true);

                        DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                        os.writeBytes(jsonObject.toString());

                        os.flush();
                        os.close();

                        Log.i("STATUS", String.valueOf(conn.getResponseCode()));
                        Log.i("MSG" , conn.getResponseMessage());


                        BufferedReader br;

                        if (200 <= conn.getResponseCode() && conn.getResponseCode() <= 299) {
                            br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        } else {
                            br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                        }

                        StringBuilder sb = new StringBuilder();
                        String output;
                        while ((output = br.readLine()) != null) {
                            sb.append(output);
                        }

                        String response = sb.toString();

                        Log.i("RRRRESPONSE UPDATE" , response);

                        conn.disconnect();

                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });

            thread.start();

        } catch (Throwable t) {
            Log.e("My App", "Could not parse malformed JSON: \"" + jsonString + "\"");
        }
    }

    private String generateBodyPostPay(String username, String subjectId, String paid, String cardsForBody) {

        String body = "{";
        body += ("\"username\":" + "\"" + username + "\",");
        body += ("\"subjectId\":" + "\"" + subjectId + "\",");
        body += ("\"paid\":" + "\"" + paid + "\",");
        body += ("\"payType\":" + "\"" + payType + "\",");
        body += ("\"cards\":" + cardsForBody);
        body += "}";

        return body;
    }
}
