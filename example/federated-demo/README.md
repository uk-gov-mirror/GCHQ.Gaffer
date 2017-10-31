Copyright 2017 Crown Copyright

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

Federated Demo
=============

## Deployment
Assuming you have Java 8, Maven and Git installed, you can build and run the latest version of the road traffic demo by doing the following:

```bash
# Clone the Gaffer repository, to reduce the amount you need to download this will only clone the master branch with a depth of 1 so there won't be any history.
git clone --depth 1 --branch master https://github.com/gchq/Gaffer.git
cd Gaffer

# This will download several maven dependencies such as tomcat.
# Using -pl we tell maven only to build the demo module and just download the other Gaffer binaries from maven.
# The -Pfederated-demo is a profile that will automatically startup a standalone instance of tomcat with the REST API and UI deployed.
mvn install -Pquick -Pfederated-demo -pl :federated-demo
```

If you wish to build all of Gaffer first then just remove the "-pl :federated-demo" part.

The rest api will be deployed to localhost:8080/rest.

To add some example data execute this json in /graph/operations/execute:

```json
{
  "class" : "uk.gov.gchq.gaffer.operation.impl.add.AddElements",
  "input" : [ {
    "group" : "BasicEntity",
    "vertex" : "1",
    "properties" : {
      "count" : 1
    },
    "class" : "uk.gov.gchq.gaffer.data.element.Entity"
  }, {
    "group" : "BasicEdge",
    "source" : "1",
    "destination" : "2",
    "directed" : true,
    "properties" : {
      "count" : 1
    },
    "class" : "uk.gov.gchq.gaffer.data.element.Edge"
  } ]
}
```

Here is an example of an advanced federated operation chain:

```json
{
   "class": "uk.gov.gchq.gaffer.operation.OperationChain",
   "operations": [
      {
         "class": "uk.gov.gchq.gaffer.federatedstore.operation.FederatedOperationChain",
         "operationChain": {
            "operations": [
               {
                  "class": "uk.gov.gchq.gaffer.operation.impl.get.GetAllElements"
               },
               {
                  "class": "uk.gov.gchq.gaffer.operation.impl.Limit",
                  "resultLimit": 1,
                  "truncate": true
               }
            ]
         }
      },
      {
         "class": "uk.gov.gchq.gaffer.operation.impl.Limit",
         "resultLimit": 1,
         "truncate": true
      }
   ]
}
```