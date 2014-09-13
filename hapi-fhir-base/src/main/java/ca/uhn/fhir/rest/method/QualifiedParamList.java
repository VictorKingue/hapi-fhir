package ca.uhn.fhir.rest.method;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 University Health Network
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.util.ArrayList;
import java.util.StringTokenizer;

import ca.uhn.fhir.model.api.IQueryParameterOr;
import ca.uhn.fhir.model.api.IQueryParameterType;

public class QualifiedParamList extends ArrayList<String> {

	private static final long serialVersionUID = 1L;
	
	private String myQualifier;

	public QualifiedParamList() {
		super();
	}
	
	public QualifiedParamList(int theCapacity) {
		super(theCapacity);
	}

	public QualifiedParamList(IQueryParameterOr<?> theNextOr) {
		for (IQueryParameterType next : theNextOr.getValuesAsQueryTokens()) {
			if (myQualifier==null) {
				myQualifier=next.getQueryParameterQualifier();
			}
			add(next.getValueAsQueryToken());
		}
	}

	public String getQualifier() {
		return myQualifier;
	}

	public void setQualifier(String theQualifier) {
		myQualifier = theQualifier;
	}

	public static QualifiedParamList singleton(String theParamValue) {
		return singleton(null, theParamValue);
	}
	
	public static QualifiedParamList singleton(String theQualifier, String theParamValue) {
		QualifiedParamList retVal = new QualifiedParamList(1);
		retVal.setQualifier(theQualifier);
		retVal.add(theParamValue);
		return retVal;
	}
	
	
	public static QualifiedParamList splitQueryStringByCommasIgnoreEscape(String theQualifier, String theParams){
		 QualifiedParamList retVal = new QualifiedParamList();
		 retVal.setQualifier(theQualifier);
		
		StringTokenizer tok = new StringTokenizer(theParams,",");
		String prev=null;
		while (tok.hasMoreElements()) {
			String str = tok.nextToken();
			if (prev!=null&&prev.endsWith("\\")) {
				int idx = retVal.size()-1;
				String existing = retVal.get(idx);
				retVal.set(idx, existing.substring(0, existing.length()-1) + "," + str);
			}else {
				retVal.add(str);
			}
			
			prev=str;
		}
		
		return retVal;
	}

	
}
