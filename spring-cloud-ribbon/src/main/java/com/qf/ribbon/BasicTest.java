package com.qf.ribbon;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;

import com.netflix.client.ClientFactory;
import com.netflix.client.http.HttpRequest;
import com.netflix.client.http.HttpResponse;
import com.netflix.config.ConfigurationManager;
import com.netflix.loadbalancer.ServerListFilter;
import com.netflix.loadbalancer.ZoneAwareLoadBalancer;
import com.netflix.niws.client.http.RestClient;

public class BasicTest {

 
	public static void main(String[] args) throws Exception {
		 
		ConfigurationManager.loadPropertiesFromResources("sample-client.properties"); // 1
		System.out.println(ConfigurationManager.getConfigInstance().getProperty("sample-client.ribbon.listOfServers"));
		RestClient client = (RestClient) ClientFactory.getNamedClient("sample-client"); // 2
		HttpRequest request = HttpRequest.newBuilder().uri(new URI("/")).build(); // 3
		for (int i = 0; i < 20; i++) {
			HttpResponse response = client.executeWithLoadBalancer(request); // 4
			System.out.println("Status code for :" + response.getStatus());
		}
		ZoneAwareLoadBalancer lb = (ZoneAwareLoadBalancer) client.getLoadBalancer();
		System.out.println(lb.getLoadBalancerStats());
		ConfigurationManager.getConfigInstance().setProperty("sample-client.ribbon.listOfServers",
				"www.qq.com:80,www.u51.xin"); // 5
		System.out.println("changing servers ...");
		Thread.sleep(3000);
		for (int i = 0; i < 20; i++) {
			HttpResponse response = client.executeWithLoadBalancer(request);
			System.out.println("Status code for :" + response.getStatus());
		}
		System.out.println(lb.getLoadBalancerStats()); // 6

	}
}
