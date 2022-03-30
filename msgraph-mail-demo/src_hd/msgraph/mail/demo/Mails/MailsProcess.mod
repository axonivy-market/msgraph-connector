[Ivy]
17F263C6C3570D4D 9.4.1 #module
>Proto >Proto Collection #zClass
Ms0 MailsProcess Big #zClass
Ms0 RD #cInfo
Ms0 #process
Ms0 @TextInP .colors .colors #zField
Ms0 @TextInP color color #zField
Ms0 @AnnotationInP-0n ai ai #zField
Ms0 @TextInP .type .type #zField
Ms0 @TextInP .processKind .processKind #zField
Ms0 @TextInP .xml .xml #zField
Ms0 @TextInP .responsibility .responsibility #zField
Ms0 @UdInit f0 '' #zField
Ms0 @UdProcessEnd f1 '' #zField
Ms0 @PushWFArc f2 '' #zField
Ms0 @UdEvent f3 '' #zField
Ms0 @UdExitEnd f4 '' #zField
Ms0 @PushWFArc f5 '' #zField
>Proto Ms0 Ms0 MailsProcess #zField
Ms0 f0 guid 17F263C6C38DAB79 #txt
Ms0 f0 method start() #txt
Ms0 f0 inParameterDecl '<> param;' #txt
Ms0 f0 outParameterDecl '<> result;' #txt
Ms0 f0 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>start()</name>
    </language>
</elementInfo>
' #txt
Ms0 f0 83 51 26 26 -14 18 #rect
Ms0 f1 211 51 26 26 0 12 #rect
Ms0 f2 109 64 211 64 #arcP
Ms0 f3 guid 17F263C6C3B7E1BC #txt
Ms0 f3 actionTable 'out=in;
' #txt
Ms0 f3 @C|.xml '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<elementInfo>
    <language>
        <name>close</name>
    </language>
</elementInfo>
' #txt
Ms0 f3 83 147 26 26 -14 15 #rect
Ms0 f4 211 147 26 26 0 12 #rect
Ms0 f5 109 160 211 160 #arcP
>Proto Ms0 .type msgraph.mail.demo.Mails.MailsData #txt
>Proto Ms0 .processKind HTML_DIALOG #txt
>Proto Ms0 -8 -8 16 16 16 26 #rect
Ms0 f0 mainOut f2 tail #connect
Ms0 f2 head f1 mainIn #connect
Ms0 f3 mainOut f5 tail #connect
Ms0 f5 head f4 mainIn #connect
