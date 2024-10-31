package com.prj4.springSendEmail.constants;

import java.util.Arrays;
import java.util.List;

public class JobQueue {
    public static final String QUEUE_DEV = "email-authen-queue";
    public static final List<String> queueNameList = Arrays.asList(QUEUE_DEV);
}