W poprzednim zadaniu zaimplementowaliśmy pobieranie listy wszystkich źródeł i usuwanie wskazanego źródła. Wobec tego implementacja aktywowania źródła danych będzie wymagała tylko dostarczenia kodu metody aktywującej. Możemy tutaj bezpiecznie wykonać tą metodę i zawsze ustawiać flagę aktywności na `true`, bo jeżeli wcześniej źródło nie było aktywne, to je aktywujemy. Gdy wcześniej było aktywne, to nic nie zrobimy. Obecna logika biznesowa nie zakłada możliwości popełnienia błędu przy tej operacji.

Implementacja metody `boolean activateSource(Long sourceId)` w klasie `KnowledgeSourceService`:

```java
    public boolean activateSource(Long sourceId) {
        KnowledgeSource knowledgeSource = knowledgeSourceRepository.findById(sourceId).orElseThrow(IllegalArgumentException::new);
        knowledgeSource.setActive(true);
        return knowledgeSource.getActive();
    }
```