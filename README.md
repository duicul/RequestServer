# RequestServer
<h1> Communication protocol client -> server</h1><br>
<table>
<th>
<td>
Client
</td>
<td>------------------------->
</td>
<td>Server
</td>
<td>--------------------------------------------->
</td>
<td>Client
</td>
<td>------------------------------------>
</td>
<td>Server
</td>
</th>
<tr>
<td>1s</td>
<td>  
</td>
<td>  req=post(outputpinstatus)
</td>
<td></td>
<td>response=json(outputpins changed) 
</td>
<td></td>
<td></td>
<td></td>
</tr> 
<tr>
<td>60s</td>
<td>
</td>
<td>  req=post(pinstatus)
</td>
<td></td>
<td>response=json(ouptutpinschanged+inputpinsdata)
</td>
<td></td>
<td>request=post(json(inputpinsvalues))
</td>
<td></td>
</tr>
 </table>