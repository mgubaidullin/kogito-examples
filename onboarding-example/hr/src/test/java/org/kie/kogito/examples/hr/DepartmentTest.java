/*
 * Copyright 2005 JBoss Inc
 *
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
 */

package org.kie.kogito.examples.hr;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.Is.is;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

@QuarkusTest
public class DepartmentTest {

    @Test
    public void testDepartmentUS() {
        evaluateForCountry("US", "John Doe");
    }

    @Test
    public void testDepartmentUK() {
        evaluateForCountry("UK", "Teresa April");
    }

    private void evaluateForCountry(String country, String result) {
        given()
                .body("{\"employee\" : {\"firstName\" : \"Mark\", \"lastName\" : \"Test\", \"personalId\" : \"xxx-yy-zzz\", \"birthDate\" : \"1995-12-10T14:50:12.123+02:00\", \"address\" : {\"country\" : \""+country+"\", \"city\" : \"Boston\", \"street\" : \"any street 3\", \"zipCode\" : \"10001\"}}}")
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .post("/department")
                .then()
                .statusCode(200)
                .body("manager", is(result));
    }
}
