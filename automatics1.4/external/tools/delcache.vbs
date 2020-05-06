appName = Wscript.Arguments.Item(0)
If((InStr(appName,"Firefox"))>0) Then
	If (appName <> "") Then
		Set oShell = CreateObject("WScript.Shell")
		If oShell.AppActivate(appName) Then
			oShell.SendKeys "+^{DEL}"
			WScript.Sleep 500
			oShell.SendKeys "{ENTER}"
		End If
	End If
Else
	If (appName <> "") Then
		Set oShell = CreateObject("WScript.Shell")
		If oShell.AppActivate(appName) Then
			oShell.SendKeys "+^{DEL}"
			WScript.Sleep 500
			oShell.SendKeys "%D"
		End If
	End If
End If
