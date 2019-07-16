# c4model
This directory contains a sample, leveraging [Structurizr for Java](https://github.com/structurizr/java)
and the [springboot extensions](https://github.com/Catalysts/structurizr-extensions/tree/master/structurizr-spring-boot) 
to create a sample microservice architecture model, based on [BigBank.plc](https://github.com/structurizr/java/blob/master/structurizr-examples/src/com/structurizr/example/BigBankPlc.java). 
This model can then be uploaded to [Structurizr](https://structurizr.com) to generate various visualizations and diagrams. 

It is meant to show a more complex example, written in a more extensible fashion (leveraging the SpringBoot extensions),
and can be used a starting point for other projects.

## Usage
### 1. Set your workspace ID, API key and secret

Make a copy of the `application.yaml.dist` file to reflect your own Structurizr workspace (this information is available
 on your dashboard, after signing in and clicking on **Show more**).
```
cp src/main/resources/application.yaml.dist src/main/resources/application.yaml
```

### 2. Run the program

Run the program, using your IDE or Gradle; for example:

```
./gradlew bootRun
```

### 3. Log onto Structurizr to see the results
Once you log in you can tweak the layout of the diagrams and save; next time you upload the elements will remember
their location.

## Modeling approach:

### More info on C4 and Structurizr

* This model is all based on the [C4 Model](https://c4model.com).
* [Structurizr](https://structurizr.com) is a service that implements the C4 model and allows you to create models 
and diagrams from those models.
* For more information on the Structurizr for java library, please see 
  * [Structurizr for Java - Getting Started](https://github.com/structurizr/java/blob/master/docs/getting-started.md)
  * [Springboot extensions](https://github.com/Catalysts/structurizr-extensions/tree/master/structurizr-spring-boot)
