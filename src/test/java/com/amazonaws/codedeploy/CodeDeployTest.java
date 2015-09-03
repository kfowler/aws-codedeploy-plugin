/*
 * Copyright 2014 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 * 
 *  http://aws.amazon.com/apache2.0
 * 
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */
package com.amazonaws.codedeploy;

import java.io.IOException;

import hudson.model.FreeStyleProject;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;
import org.xml.sax.SAXException;

import com.gargoylesoftware.htmlunit.WebAssert;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class CodeDeployTest {

    @Rule
    public JenkinsRule j = new JenkinsRule();

    @Test
    public void testConfigurePage() throws IOException, SAXException {
        HtmlPage page = j.createWebClient().goTo("configure");
        WebAssert.assertTextPresent(page, "AWS CodeDeploy");
    }

    @Test
    public void canConfigure() throws Exception {
        FreeStyleProject project = j.createFreeStyleProject();
        AWSCodeDeployPublisher before =
            new AWSCodeDeployPublisher(
                "s3bucket",
                "s3prefix",
                "applicationName",
                "deploymentGroupName",
                "deploymentConfig",
                "region",
                true, // waitForCompletion
                300L,  // pollingTimeoutSec
                15L,   // pollingFreqSec
                "credentials",
                "awsAccessKey",
                "awsSecretKey",
                "iamRoleArn",
                "externalId",
                "includes",
                "proxyHost",
                0,
                "excludes",
                "subdirectory");
        project.getPublishersList().add(before);
        j.submit(j.createWebClient().getPage(project, "configure").getFormByName("config"));
        AWSCodeDeployPublisher after = project.getPublishersList().get(AWSCodeDeployPublisher.class);
        j.assertEqualBeans(before, after, "deploymentGroupName,deploymentConfig");
    }

    @Test
    public void groupCountMustMatchConfigCount() throws Exception {
        FreeStyleProject project = j.createFreeStyleProject();
        AWSCodeDeployPublisher before =
            new AWSCodeDeployPublisher(
                "s3bucket",
                "s3prefix",
                "applicationName",
                "deploymentGroup1,deploymentGroup2",
                "deploymentConfig",
                "region",
                true, // waitForCompletion
                300L,  // pollingTimeoutSec
                15L,   // pollingFreqSec
                "credentials",
                "awsAccessKey",
                "awsSecretKey",
                "iamRoleArn",
                "externalId",
                "includes",
                "proxyHost",
                0,
                "excludes",
                "subdirectory");
        project.getPublishersList().add(before);
        j.submit(j.createWebClient().getPage(project, "configure").getFormByName("config"));
        AWSCodeDeployPublisher after = project.getPublishersList().get(AWSCodeDeployPublisher.class);
        j.assertEqualBeans(before, after, "deploymentGroupName,deploymentConfig");
    }

}
