%token TO NOUN DET INT Prep Verb phrasalVerb AUX presentparticiple pastparticiple ADj LinkingVgp
%token INT STRING_VALUE NEWLINE ELSE

%%
statement:
            subject predicate              
;

subject:
            NP    
;



NP:
             NOUN				 
             |DET NP                            
             |AP NP                             
             |NP PP                             

;

AP:
                 ADj                             
                 |INT ADj                         
;

PP:          
             Prep  NP                             

;

predicate:
             VP        
;

VP: 
             Vgp                                         			{Intrasitive}
             |Vgp NP         										{Transitive}
             |Vgp NP TO NP                                   		{Ditransitive}
             |LinkingVgp NP               							{Intensive}
             |LinkingVgp AP                                        	{Intensive}
             |LinkingVgp PP                 						{Intensive}
             |Vgp NP NP                                            	{Complex_transitive}
             |Vgp NP AP                                             {Complex_transitive}
             |Vgp NP PP                               				{Complex_transitive}
             |Vgp PP                                        		{Propositional}
             |Vgp NP NonFinite                          			{Non_finite}
;

Vgp: 
        Verb                                   
        |phrasalVerb                                   
        |AUX Verb                                     
        |AUX phrasalVerb                                   
;

NonFinite:
               presentparticiple                              
                  |TO VP                                            
		  |pastparticiple                              
;
%%