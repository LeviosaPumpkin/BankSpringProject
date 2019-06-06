package currencyratepack;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import daopack.BankDao.Currency;

public class CurrencyRate {
	
	public static final double NO_CURRENCY = -1.0;

	public static double getRate(int currency){
		double rate = 0.0;
		// Setting URL
		String url_str = "http://iss.moex.com/iss/statistics/engines/currency/markets/selt/rates.json";

		HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(url_str);

        try {
            HttpResponse response = client.execute(request);
            HttpEntity entity = response.getEntity();

            // Read the contents of an entity and return it as a String.
            String content = EntityUtils.toString(entity);
            JSONObject result = new JSONObject(content);
            
            JSONArray headers = result.getJSONObject("cbrf").getJSONArray("columns");
            List<String> headersList = new ArrayList<>();
            for(int i = 0; i < headers.length(); ++i) {
            	headersList.add(headers.getString(i));
            }
            
            int index = -1;
            
            if(currency == Currency.USD.getId()) index = headersList.indexOf("CBRF_USD_LAST");
            else if (currency == Currency.EUR.getId()) index = headersList.indexOf("CBRF_EUR_LAST");
            else {
            	System.out.println("Invalid currency");
            	return NO_CURRENCY;
            }
            
            JSONArray data = result.getJSONObject("cbrf").getJSONArray("data");
            rate = data.getJSONArray(0).getDouble(index);
            System.out.println(rate);
            
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
			e.printStackTrace();
		}
		return rate;
	}
}
