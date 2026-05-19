# Pede a descrição da migration se não for passada como argumento
param (
    [Parameter(Mandatory=$true, HelpMessage="Digite a descrição da migration (ex: criar_tabela_clientes)")]
    [string]$Descricao
)

# Define o caminho padrão das migrations no Spring Boot
$DiretorioDestino = "src\main\resources\db\migration"

# Cria a pasta caso ela ainda não exista
if (!(Test-Path -Path $DiretorioDestino)) {
    New-Item -ItemType Directory -Path $DiretorioDestino | Out-Null
    Write-Host "Criando estrutura de pastas: $DiretorioDestino" -ForegroundColor Cyan
}

# Gera o timestamp no formato YYYYMMDDHHMMSS
$Timestamp = Get-Date -Format "yyyyMMddHHmmss"

# Substitui espaços por underscores (caso você digite "criar tabela" em vez de "criar_tabela")
$DescricaoFormatada = $Descricao -replace ' ', '_'

# Monta o nome do arquivo final
$NomeArquivo = "V${Timestamp}__${DescricaoFormatada}.sql"
$CaminhoCompleto = Join-Path -Path $DiretorioDestino -ChildPath $NomeArquivo

# Cria o arquivo vazio
New-Item -ItemType File -Path $CaminhoCompleto | Out-Null

Write-Host "Arquivo de migration criado com sucesso!" -ForegroundColor Green
Write-Host "Caminho: $CaminhoCompleto" -ForegroundColor Yellow