[Ivy]
17F40684A56F5FEF 9.4.1 #module
>Proto >Proto Collection #zClass
Tr0 TestHelper Big #zClass
Tr0 B #cInfo
Tr0 #process
Tr0 @TextInP .colors .colors #zField
Tr0 @TextInP color color #zField
Tr0 @AnnotationInP-0n ai ai #zField
Tr0 @TextInP .type .type #zField
Tr0 @TextInP .processKind .processKind #zField
Tr0 @TextInP .xml .xml #zField
Tr0 @TextInP .responsibility .responsibility #zField
Tr0 @StartRequest f0 '' #zField
Tr0 @EndTask f1 '' #zField
Tr0 @GridStep f3 '' #zField
Tr0 @PushWFArc f4 '' #zField
Tr0 @PushWFArc f2 '' #zField
>Proto Tr0 Tr0 TestHelper #zField
Tr0 f0 outLink start.ivp #txt
Tr0 f0 inParamDecl '<String environment> param;' #txt
Tr0 f0 inParamTable 'out.environment=param.environment;
' #txt
Tr0 f0 requestEnabled true #txt
Tr0 f0 triggerEnabled false #txt
Tr0 f0 callSignature start(String) #txt
Tr0 f0 caseData businessCase.attach=true #txt
Tr0 f0 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>start.ivp</name>
    </language>
</elementInfo>
' #txt
Tr0 f0 @C|.responsibility Everybody #txt
Tr0 f0 81 49 30 30 -22 17 #rect
Tr0 f1 337 49 30 30 0 15 #rect
Tr0 f3 actionTable 'out=in;
' #txt
Tr0 f3 actionCode ivy.task.getApplication().setActiveEnvironment(in.environment); #txt
Tr0 f3 168 42 112 44 0 -8 #rect
Tr0 f4 111 64 168 64 #arcP
Tr0 f2 280 64 337 64 #arcP
>Proto Tr0 .type com.axonivy.connector.office365.test.Data #txt
>Proto Tr0 .processKind NORMAL #txt
>Proto Tr0 0 0 32 24 18 0 #rect
>Proto Tr0 @|BIcon #fIcon
Tr0 f0 mainOut f4 tail #connect
Tr0 f4 head f3 mainIn #connect
Tr0 f3 mainOut f2 tail #connect
Tr0 f2 head f1 mainIn #connect
