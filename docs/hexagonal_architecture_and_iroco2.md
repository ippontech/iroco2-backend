# Hexagonal architecture in IroCO₂

## Implementation

```mermaid
block-beta
    block:b1
        columns 1
        Primary
        adapter1("Adapters:
        - are in the module 'infrastructure'
        - are in a package 'primary'
        - have a suffix 'Controller'")
    end

    block:bd
        columns 1
        Domain
        block
            port1("Port for primary:
            - are in the module 'domain'
            - are in a package 'primary'
            - have a suffix Svc")
            port2("Port for secondary:
            - are in the module 'domain'
            - are in a package 'secondary'
            - have a suffix 'Storage'")
        end
    end

    block:b2
        columns 1
        Secondary
        adapter2("Adapters:
        - are in the module 'infrastructure'
        - are in a package 'secondary'
        - have a suffix 'StorageAdapter'")
    end

    adapter1 --"use"--> port1
    adapter2 --"implement"--> port2

    classDef title stroke:none,font-style:italic,font-weight:bold
    class Primary,Domain,Secondary title

    style Primary fill:transparent,color:blue
    style b1 fill:transparent,stroke:#C0C5CC
    style adapter1 fill:#E0E7EE

    style Domain fill:transparent,color:red
    style bd fill:transparent,stroke:#EE9069
    style port1 fill:#EE9069
    style port2 fill:#EE9069

    style Secondary fill:transparent,color:green
    style b2 fill:transparent,stroke:#00E989
    style adapter2 fill:#00E989
```

## Architectural rules

In order to ensure hexagonal architecture in IroCO₂, some tests are added:
- in domain:
  - Classes in a package secondary are interfaces
  - Classes in a package primary are interfaces
  - No adapter classes should exist
- in infrastructure:
  - and domain:
    - No classes should access to implementation services of domain (@DomainService)
    - No classes should depend on classes in functional packages
  - primary:
    - Adapters should access to primary classes of domain
    - Adapters should be in a primary package
    - And its beans
  - secondary:
    - Adapters should implement secondary interfaces of domain
    - Adapters should be annotated with @Repository
    - Adapters should be in a secondary package
    - And its entities

These rules are implemented with _archunit_.