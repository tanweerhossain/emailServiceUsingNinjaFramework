/**
 * Copyright (C) 2012-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * Copyright (C) 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package controllers;

import ninja.Context;
import ninja.Result;
import ninja.Results;

import java.io.IOException;

import com.google.gson.Gson;
import com.google.inject.Singleton;


@Singleton
public class ApplicationController {

    public Result error() {

        return Results.html().template("views/system/404notFound.ftl.html");

    }
    
    public Result index(Context context)  {
    	Status status = new Status();
    	Gson gson = new Gson();
    	Transaction transaction;
    	StringBuilder sb = new StringBuilder();
		String s;

		//convert json data to string format
		try {
			while ((s = context.getReader().readLine()) != null) {
					sb.append(s);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		transaction = (Transaction) gson.fromJson(sb.toString(), Transaction.class);

    	System.out.println("=================>"+ context.getHeader("authorization").split(" ")[1]);
    	System.out.println("=================>"+ sb.toString());
    	
    	try {
        	status = new JsonParserServlet().doPost(transaction, context.getHeader("authorization").split(" ")[1]);
		} catch (IOException e) {
			// TODO: handle exception
		}    	
    
        return Results.json().render(status);

    }
    
}
