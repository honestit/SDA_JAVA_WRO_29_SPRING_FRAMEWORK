Usunięcie źródła wiedzy wymaga od nas upewnienia się, że źródło wiedzy jest nieaktywne, bo taka istnieje zasada biznesowa. Moglibyśmy również sprawdzać czy źródło wiedzy nie jest przypadkiem przez kogoś znane, ale lepiej pilnować, aby użytkownik nie mógł potwierdzić nieaktywnego źródła wiedzy. Mając spójność między operacją dodawania źródła wiedzy, aktywowania go, usuwania i potwierdzania możemy wszystkie te trzy metody uprościć.

W ramach tego zadania napiszemy również metodę, która zwraca listę wszystkich źródeł wiedzy, bo potrzebujemy jej do prawidłowego działania operacji usuwania, dodawania i aktywowania źródeł wiedzy.

Implementacja metody `List<KnowledgeSourceDTO> getAllSource` z klasy `KnowledgeSourceService`:

```java
    public List<KnowledgeSourceDTO> getAllSources() {
        return knowledgeSourceRepository.findAll().stream().map(sourceEntity -> {
            KnowledgeSourceDTO sourceDTO = new KnowledgeSourceDTO();
            sourceDTO.setId(sourceEntity.getId());
            sourceDTO.setName(sourceEntity.getName());
            sourceDTO.setDescription(sourceEntity.getDescription());
            sourceDTO.setUrl(sourceEntity.getUrl());
            sourceDTO.setActive(sourceEntity.getActive());
            sourceDTO.setConnectedSkills(sourceEntity.getConnectedSkills().stream().map(skillEntity -> {
                SkillDTO skillDTO = new SkillDTO();
                skillDTO.setId(skillEntity.getId());
                skillDTO.setName(skillEntity.getName());
                skillDTO.setCategory(skillEntity.getName());
                return skillDTO;
            }).collect(Collectors.toSet()));
            return sourceDTO;
        }).collect(Collectors.toList());
    }
```

Implementacja właściwej metody do usuwania, a więc `boolean deleteSource(Long sourceId)` w klasie `KnowledgeSourceService`:

```java
    public boolean deleteSource(Long sourceId) {
        KnowledgeSource knowledgeSource = knowledgeSourceRepository.findById(sourceId).orElseThrow(IllegalArgumentException::new);
        if (knowledgeSource.getActive()) {
            return false;
        }
        knowledgeSourceRepository.delete(knowledgeSource);
        return true;
    }
```