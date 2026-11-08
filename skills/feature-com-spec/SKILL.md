# Skill: Feature com Spec-Driven Development (SDD)

## Objetivo
Implementar uma nova feature (funcionalidade) em um projeto, garantindo máxima previsibilidade e controle, através do fluxo contínuo: Especificação -> Plano -> Implementação Incremental.

## Entradas
- Descrição da feature desejada (Regras de negócio ou caso de uso).
- Repositório do projeto com o código fonte inicial.

## Passo a Passo

1. **Etapa 2 (AGENTS.md):** O desenvolvedor (ou agente) varre o repositório e cria/atualiza o `AGENTS.md` definindo stack, estrutura, como testar, convenções e restrições.
2. **Etapa 3 (Especificação):** Cria-se o `docs/spec.md`. Nele, define-se o objetivo, regras de negócio precisas, critérios de aceite (cenários Dado/Quando/Então), impactos no sistema existente e, crucialmente, o que está FORA do escopo.
3. **Etapa 4 (Plano de Implementação):** A especificação é quebrada em pequenas tarefas sequenciais no `docs/plano.md`. Cada tarefa indica o que será feito e quais arquivos serão tocados.
4. **Etapa 5 (Implementação Incremental):** Escreve-se o código estritamente necessário para uma tarefa do plano. Valida-se com testes e uso manual. Marca-se um `[X]` no plano e faz-se um *Commit Semântico*. Repete-se o processo até zerar o plano.
5. **Etapa 6 (Validação Final):** Executa-se toda a suite de testes e varre-se novamente os critérios de aceite.

## Regras Absolutas
- NENHUMA linha de código deve ser escrita antes da aprovação total do `spec.md` e do `plano.md`.
- Uma tarefa do plano implementada por vez.
- Um commit atômico por tarefa implementada.
- Não refatorar funcionalidades que estejam fora do escopo definido na Etapa 3.

## Modelos
- O `spec.md` exige cabeçalhos: `Contexto e Objetivo`, `Regras de Negócio`, `Critérios de Aceite`, `Impacto` e `Fora do Escopo`.
- O `plano.md` exige checklist: `- [ ] Tarefa N: Resumo - O que fazer | Onde alterar | Como testar`.