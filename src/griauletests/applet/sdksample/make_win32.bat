@echo off

rem -------------------------------------------------------------------------------
rem  Fingerprint SDK Sample
rem  (c) 2005-2010 Griaule Biometrics Ltda.
rem  http://www.griaulebiometrics.com
rem  -------------------------------------------------------------------------------
rem 
rem  This sample is provided with "Fingerprint SDK Recognition Library" and
rem  can't run without it. It's provided just as an example of using Fingerprint SDK
rem  Recognition Library and should not be used as basis for any
rem  commercial product.
rem 
rem  Griaule Biometrics makes no representations concerning either the merchantability
rem  of this software or the suitability of this sample for any particular purpose.
rem 
rem  THIS SAMPLE IS PROVIDED BY THE AUTHOR "AS IS" AND ANY EXPRESS OR
rem  IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
rem  OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
rem  IN NO EVENT SHALL GRIAULE BE LIABLE FOR ANY DIRECT, INDIRECT,
rem  INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
rem  NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
rem  DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
rem  THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
rem  (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
rem  THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
rem 
rem  You can download the trial version of Fingerprint SDK directly from Griaule website.
rem                                                                    
rem  These notices must be retained in any copies of any part of this
rem  documentation and/or sample.
rem 
rem  -------------------------------------------------------------------------------

rem This batch file compiles Fingerprint SDK Java Applet Sample
rem It will try finding all necessary files given same basic info:
rem - variable INSTALLDIR (where the required DLLs will be zipped)
rem - variable LICENSEDIR (where the license can be found)
rem - variable DLLDIR     (where the required DLLs/JARs can be found)
rem - variable OUTPUTDIR  (where the applet JAR will be created)

rem -------------------Setting Variables---------------------------------
set INSTALLDIR=InstallTemp
set LICENSEDIR=..\..\..\..\common
set DLLDIR=..\..\..\bin
set OUTPUTDIR=bin
rem ---------------------------------------------------------------------

rmdir /s /q "%INSTALLDIR%"
mkdir "%INSTALLDIR%"


echo ----Zipping the required DLLs
mkdir "%INSTALLDIR%\libs"
copy /b /y "%DLLDIR%\*.dll" "%INSTALLDIR%\libs"
copy /b /y "%DLLDIR%\GrTools.exe" "%INSTALLDIR%\libs"
copy /b /y "%LICENSEDIR%\GrFingerLicenseAgreementTrial.txt" "%INSTALLDIR%\libs\GrFingerLicenseAgreement.txt"
"%JAVA_HOME%\bin\jar" cfM "%INSTALLDIR%\FingerprintSDKLibs.zip" -C "%INSTALLDIR%\libs" .
rmdir /s /q "%INSTALLDIR%\libs"
echo.


echo ----Compiling the Java Applet
"%JAVA_HOME%\bin\javac" -source 1.4 -target 1.4 -nowarn -classpath "%DLLDIR%\grfingerjava.jar" com\griaule\fingerprintsdk\appletsample\*.java -d "%INSTALLDIR%"
echo.


echo ----Creating Applet Jar File
"%JAVA_HOME%\bin\jar" cf FingerprintSDKJavaAppletSample.jar -C "%INSTALLDIR%" com -C "%INSTALLDIR%" FingerprintSDKLibs.zip
echo.


echo ----Creating Temporary RSA Key
if not exist ..\..\..\..\..\..\cert\java goto GENKS
copy /b /y ..\..\..\..\..\..\cert\java\java.keystore GrTmpRSAKey
goto SIGNJAR
:GENKS
"%JAVA_HOME%\bin\keytool" -genkey -alias applet -keystore GrTmpRSAKey -keypass secret -dname "cn=GrFinger Sample Applet" -storepass secret
echo.


:SIGNJAR
echo ----Signing the applet JAR (KeyStore Password: "secret")
if not exist "%OUTPUTDIR%" mkdir "%OUTPUTDIR%"
"%JAVA_HOME%\bin\jarsigner" -keystore GrTmpRSAKey -signedjar "%OUTPUTDIR%\SignedFingerprintSDKJavaAppletSample.jar"   FingerprintSDKJavaAppletSample.jar applet
"%JAVA_HOME%\bin\jarsigner" -keystore GrTmpRSAKey -signedjar "%OUTPUTDIR%\SignedFingerprintSDKJava.jar"               "%DLLDIR%\grfingerjava.jar"  applet
echo.


echo ----Removing temporary files
del /q GrTmpRSAKey
del /q FingerprintSDKJavaAppletSample.jar
rmdir /s /q "%INSTALLDIR%"
echo.
pause

@echo on