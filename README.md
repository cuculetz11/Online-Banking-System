# Etapa 1 - J. POO Morgan Chase & Co.
Cucu Viorel-Cosmin 324CA

---
## Design Patterns
### Visitor
Am folosit visior pentru a satisface principiul open/close pentru clasa "Bank"(**OCP**). Dupa cum se poate vedea in "Bank" nu am nicio metoda, clasa fiind curata si simpla. Deci am folosit visitor doar pentru a nu polua banca cu metode si a face cate o clasa dedicate fiecarei functionalitati(**SRP**). Banca este vizitata de urmatoarele clase:
- **UserInitialize**: ce aduga userii in banca
- **ExchangeRatesInitialize**: aduaga tot cursul valutar in banca intr-un mod specific pentru a face usor apoi DFS
- **CommerciantInitialize**: aduga toti comerciantii dati ca input in banca(s-a adugat la etapa2, avnd acest visitor a fost lejer sa ii adug)
- **PaymentMethod**: contine o stategie cu care visizteaza banca pentru a realiza uo anumita plata
---
### Singleton
L-am folosit pentru urmatoarele clase:
- **Bank**: l-am folosit pentru a accesa toate atributele bancii de oriunde din ptogram si clar pentru unicitate fiind doar o banca
- **JsonOutManager**: pentru a aduga output-ul de oriunde din aplicatie
---
### Factory
Acest designmi se pare elementar, l-am folosit in multe locuri. Prin acest design reusesti sa-ti extinzi codul foarte usor fara a face modificari in codul principal ce doloseste acel factory.
- **CommandManager**: in functie de mumele comenzii se creaza o anumita comanda ce e apoi executata
- **AccountFactory**: in functie de tipul de cont se creaza acel cont
- **CardFactory**: in functie de tipul de card se creaza acel card
- **TransactionManager**: in functe de numele tranzactiei se creeaza si se adauga aceasta in isoricul trnzactiilor userului si/sau contului
- **CommissionFactory**: in functie de plan se creaza o clasa de tip **CommissionPlan** ce contine logica de obtinere a comisionului specific planului
---
### Strategy 
Acest pattern este foarte util atunci cand ai mai multe strategi pe care poti sa le folosesti pentru a face acelasi lucru, dar intr-un mod diferit. Te sacapa de un switch infernal si e foarte lejer sa extinzi.
- **PaymentMethod**: este contextul ce utilizeaza diferite stategi de a face o plata:
  - ***PaymentStrategy*** reprezinta interfata ce contine metodele pentru verifica daca plata e posibila si pentru a efectua plata. Aceasta metoda este implemntata de urmatoarele strategii: 
      - ***CardPaymentStrategy*** ce reprezinta stategia pentru plata cu cardul

      - ***BankTransferStrategy*** ce reprezinta strategia pentru transferul bancar

      - ***WithdrawCashStrategy*** reprezinta strategia pentru scoaterea baniilor de pe card; aceasta stategie a fost inclusa acum datorita faptului ca aveam deja acest design a fost foarte usor sa extind codul
      - ***WithdrawSavingsStrategy*** reprezinta strategia prin care scoatem bani de pe un cont de tip **savings** si o adugam in cel **clasic**

- **CashBackContext** aici am incercat cumva sa fac strategy pattern nu e chiar exact cum trebuie, dar il pun aici; reprezinta contextul ce e continut in clasa **UserPlan**. In momentul in care se efectuaza o tranzactie catre un comerciant se face acest context ca apoi acesta sa creeze strategia rprezentativa acelui comerciant si sa verifice, aduge cashback-ul.
  - ***CashbackStrategy*** reprezinta interfata ce contine metode pentru verificarea adugarii cashback-ului, adugarea cashback-ului si actualizarea evidentei acestuia.
    - ***NrofTransactionCashback*** reprezinta strategia ce implementeaza logica pentru acest tip de cashback
    - ***SpendingThresholdCashback*** reprezinta strategia ce implementeaza logica pentru acest tip de cashback
---
### Builder
Am folosit acest design pentru clasa ***DatesForTransaction*** pentru a transmite doar datele necesare fiecarei tranzactii ce urmeaza sa fie generate. M-am cam complicat cu acest lucru pentru ca prctic as fi putut aplica direct pe tranzactie acest deign dar apoi aveam codul mai dezordonat. Asa cum am facut acum vad clar fiecare tranzactie ce fel arata si unde este adugata(isoricul de tranzactii al userului si/sau al conului respectiv).

---
### Command
Am folosit acest pattern pentru a executa comenziile date de la input. Impreuna cu **factory pattern** face ca orice noua comanda sa fie foarte usor de adaugat. Avem interfata de are o metoda de execute(), apoi clasele concrete ce extind aceasta interfata. In **CommandManager** eu creeez acea comanda conccreta pe care o returnez in clasa **BankServices** unde o execut. Pentru a aduga o noua comanda pur si simplu trebuie adugata o linie in CommandManagersi o trebuie facuta o clasa ce implementeaza interfata Command
- ***Command*** reprezinta interfata ce defineste metoda de **execute()** care va fi implemntata de comenzile concrete
- exemple de comenzi concrete: ***AcceptSplitPayment***, ***AddAccount***, ***ashWithdrawal***, ...

Observer facusem cand balantele aveau doar 2 zecimale. Facusem o interfata ce avea o metoda, fiecare cont implementa aceea interfata, metoda respectiva facea ca balnta sa se rotunjeasca, Apoi am facut un vistor pentru banca ce lua fiecare cont si aplica aceasta metoda(un updateAll). Dar apoi cand s-a modificat checkerul am renuntat pentru ca strica precizia.

---