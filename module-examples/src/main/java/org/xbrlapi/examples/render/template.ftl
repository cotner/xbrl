[#ftl]

<html xmlns="http://www.w3.org/1999/xhtml"
		xmlns:xlink="http://www.w3.org/1999/xlink"
		xmlns:iso4217="http://www.xbrl.org/2003/iso4217"
		xmlns:ix="http://www.xbrl.org/2008/inlineXBRL"
		xmlns:link="http://www.xbrl.org/2003/linkbase"
		xmlns:xbrli="http://www.xbrl.org/2003/instance"
		xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">

	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title>XBRL Inline Document</title>
		<link rel="stylesheet" type="text/css" href="stylesheet.css"/>
		<meta content="text/html; charset=utf-8" http-equiv="Content-Type" />
	</head>

  <body>

		<div class="header">
			<div class="pagetitle">
	  		<div class="normal">Rendered XBRL Report</div>
			</div>
		</div>
	
		<div style="display: none">
		  <ix:header>
	  		<ix:resource>
	        [#list contexts as fragment]
              [#assign text = store.serializeToString(store.getSubtree(fragment)) /]
	          ${text?substring(text?index_of("\n"))}
	        [/#list]
	        [#list units as fragment]
              [#assign text = store.serializeToString(store.getSubtree(fragment)) /]
              ${text?substring(text?index_of("\n"))}
	        [/#list]
			  </ix:resource>
			  <ix:reference>
	        [#list schemaReferences as fragment]
              [#assign text = store.serializeToString(store.getSubtree(fragment)) /]
              ${text?substring(text?index_of("\n"))}
	        [/#list]
	        [#list linkbaseReferences as fragment]
              [#assign text = store.serializeToString(store.getSubtree(fragment)) /]
              ${text?substring(text?index_of("\n"))}
	        [/#list]
			  </ix:reference>
	    </ix:header>
	  </div>

    <div class="main">
  
      <!-- Generate a table here for each presentation structure. -->
  
      [#list tables as table]
        
        <br class="indent"/>
        
        <table class="value" style="width: 900.0px;">
          <caption class="value">${table.title}</caption>
          <colgroup>
            <!-- Produce a column for each indentation -->
            [#list 0..table.maxLevel as i]
              <col style="width: 20.0px;"/>
            [/#list]
            <!-- Produce a column for each context -->
	        [#list table.periods as period]
	          <col />
	        [/#list]
          </colgroup>
    
          <!-- Column headings for contexts --> 
    	  <tr>
    	    <!-- Indentation columns -->
            [#list 0..table.maxLevel as i]
              <th class="indent" colspan="1" rowspan="1"/>
            [/#list]

            <!-- Context heading labels -->
            [#list table.periods as period]
              <th class="value" colspan="1" rowspan="1">
                <div class="normal">
                  ${period.label}
                </div>
              </th>
            [/#list]
    	  </tr>

    	    [#assign counter = 1 /]
            [#list 0..(table.concepts?size-1) as cIndex]

    	      [#assign concept = table.concepts[cIndex] /]
              [#assign label = table.labels[cIndex] /]

              [#if counter = 1]
                [#assign counter = 0 /]
                [#assign classPrefix = "row1" /]
              [#else]
                [#assign counter = 1 /]
                [#assign classPrefix = "row2" /]
              [/#if]

              [#if label?lower_case?trim?ends_with(", total")]
                <tr class="${classPrefix}total">
              [#else]
                <tr class="${classPrefix}">
              [/#if]
    
              [#assign continuing = true /]
              [#assign indent = true /]
              [#assign value = false /]
              [#assign level = 0 /]
              [#list 0..table.maxLevel as i]<!-- Loop over indentation levels -->
                [#if continuing]
	              [#if label[i] = " " && indent]
	                [#assign level = level + 1 /]
	                  <td class="indent" colspan="1"/>
	              [#else]
	                [#assign indent = true /]
	                [#if ! value]
                      <td class="label" colspan="${table.maxLevel+1-level}">
                        ${label?trim}
                      </td>
                      [#assign value = true /]
	                [#else]
                      [#assign continuing = false /]
	                [/#if]
	              [/#if]
	            [/#if]
              [/#list]<!-- Loop over indentation levels -->
              
              [#assign conceptId = concept.targetNamespaceURI + ": " + concept.name /]
              [#assign conceptAspect = table.aspectModel.getAspect("concept") /]
              [#assign conceptHasData = conceptAspect.hasValue(conceptId) /]
              [#if conceptHasData]
                [#assign conceptValue = conceptAspect.getValue(conceptId) /]
                ${conceptAspect.setSelectionCriterion(conceptValue)}
              [/#if]
              
              [#list table.periods as period]

                [#if conceptHasData]
                  ${period.aspect.setSelectionCriterion(period)}
                  [#assign items = table.aspectModel.matchingFacts /]
                [#else]
                  [#assign items = [] /]
                [/#if]

                [#if conceptHasData && items?exists && items?size > 0]
                    [#assign item = items[0] /]
                    [#if ! item.nil]
	                    <td class="value">
		                    [#if item.numeric]<!-- The item is numeric -->
		                      <div class="numeric">
		                          <!-- Sort out the inline XBRL stuff. -->
		                          <!-- Sort out the formatting to take unit information into account. -->
		                          <ix:nonFraction 
		                              xmlns="${concept.targetNamespaceURI}"
		                              ix:name="${concept.name}"
		                              ix:format="commadot" 
		                              ix:scale="0"
		                              contextRef="${item.contextId}" 
		                              unitRef="${item.unitId}"
		                              decimals="0" 
		                              id="${item.fragmentIndex}" >
                                      [#assign nMeasures = item.unit.resolvedNumeratorMeasures /]
                                      [#assign dMeasures = item.unit.resolvedDenominatorMeasures /]
                                      [#if nMeasures?size > 1 || dMeasures?size > 0]
                                        ${item.value}
                                      [/#if]
                                      [#assign measure = nMeasures[0] /]
                                      [#if measure.namespace = iso4217]
                                        ${item.value?number?string.currency} ${measure.localname}
                                      [/#if]
		                              [#if measure.namespace == xbrli && measure.localname == "shares"]
                                        ${item.value?number} shares
		                              [/#if]
                                      [#if measure.namespace == xbrli && measure.localname == "pure" && (item.value?number >= 1)]
                                        ${item.value?number}
                                      [/#if]
                                      [#if measure.namespace == xbrli && measure.localname == "pure" && (item.value?number < 1)]
                                        ${item.value?number * 100} %
                                      [/#if]
		                          </ix:nonFraction>
		                      </div>
		                    [#else]<!-- The item is non-numeric -->
		                      <div class="nonnumeric">
			                      <!-- Sort out the inline XBRL stuff. -->
			                      [#assign value = item.value /]
			                      [#if value?length > 255]
			                        ${value?substring(0,255)} ...
			                      [#else]
			                        ${value}
			                      [/#if]
		                      </div>
		                    [/#if]
	                    </td>
	                [#else]
		                  <td class="value">nil</td>
                    [/#if]
                [#else]
                  <td class="value"/>
                [/#if]
              [/#list]<!-- loop over periods -->
              </tr>
    	    [/#list]<!-- Loop over concepts - one per row -->
    	    
  	    </table>
      [/#list]<!--  Loop over presentation network tables -->

    </div><!-- Main div -->

    <hr />

    <div class="footer">
      ${now}
    </div>

  </body>

</html>

